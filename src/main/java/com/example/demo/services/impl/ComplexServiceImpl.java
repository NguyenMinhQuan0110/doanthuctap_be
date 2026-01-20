package com.example.demo.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.request.ComplexRequest;
import com.example.demo.dtos.response.ComplexResponse;
import com.example.demo.dtos.response.ComplexResponseDistance;
import com.example.demo.entites.Complex;
import com.example.demo.entites.District;
import com.example.demo.entites.Province;
import com.example.demo.entites.User;
import com.example.demo.entites.enums.ComplexStatus;
import com.example.demo.entites.enums.PitchType;
import com.example.demo.repositories.ComplexRepository;
import com.example.demo.repositories.DistrictRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.ComplexService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComplexServiceImpl implements ComplexService{
	
	@Value("${upload.complex-dir}")
    private String avatarComDir;
	
	@Value("${server.url}")
    private String serverUrl;
	
    @Autowired
    private ComplexRepository complexRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<ComplexResponse> getAllComplexes() {
        return complexRepository.findAll()
                .stream()
                .filter(c -> c.getStatus() == ComplexStatus.active) // ✅ chỉ lấy sân đang hoạt động
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ComplexResponse> getActiveComplexes() {
        List<Complex> complexes = complexRepository.findByStatus(ComplexStatus.active);
        return complexes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public ComplexResponse getComplexById(Integer id) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));
        return mapToResponse(complex);
    }

    @Override
    public ComplexResponse createComplex(ComplexRequest request) {
    	// ✅ Kiểm tra trùng tên cụm sân
        if (complexRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Complex name already exists");
        }
        
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found"));

        Complex complex = new Complex();
        complex.setName(request.getName());
        complex.setAddress(request.getAddress());
        complex.setLatitude(request.getLatitude());
        complex.setLongitude(request.getLongitude());
        complex.setPhone(request.getPhone());
        complex.setOwner(owner);
        complex.setDistrict(district);

        return mapToResponse(complexRepository.save(complex));
    }

    @Override
    public ComplexResponse updateComplex(Integer id, ComplexRequest request) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        // ✅ Kiểm tra trùng tên (bỏ qua chính nó)
        boolean nameExists = complexRepository.existsByName(request.getName());
        if (!complex.getName().equals(request.getName()) && nameExists) {
            throw new RuntimeException("Complex name already exists");
        }

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found"));

        complex.setName(request.getName());
        complex.setAddress(request.getAddress());
        complex.setLatitude(request.getLatitude());
        complex.setLongitude(request.getLongitude());
        complex.setPhone(request.getPhone());
        complex.setOwner(owner);
        complex.setDistrict(district);

        return mapToResponse(complexRepository.save(complex));
    }

    @Override
    public void deleteComplex(Integer id) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));
        complexRepository.delete(complex);
    }

    private ComplexResponse mapToResponse(Complex complex) {
    	
    	String avatarComUrl = null;
        if (complex.getAvatarCom() != null && !complex.getAvatarCom().isEmpty()) {
        	avatarComUrl = serverUrl + complex.getAvatarCom();
        }
    	
        ComplexResponse response = new ComplexResponse();
        response.setId(complex.getComplexId());
        response.setName(complex.getName());
        response.setAddress(complex.getAddress());
        response.setLatitude(complex.getLatitude());
        response.setLongitude(complex.getLongitude());
        response.setPhone(complex.getPhone());
        response.setStatus(complex.getStatus());
        response.setCreatedAt(complex.getCreatedAt());
        response.setAvatarCom(avatarComUrl);

        if (complex.getOwner() != null) {
            response.setOwnerId(complex.getOwner().getUserId());
            response.setOwnerName(complex.getOwner().getFullName());
        }

        if (complex.getDistrict() != null) {
            response.setDistrictId(complex.getDistrict().getDistrictId());
            response.setDistrictName(complex.getDistrict().getDistrictName());
            if (complex.getDistrict().getProvince() != null) {
                Province province = complex.getDistrict().getProvince();
                response.setProvinceId(province.getProvinceId());
                response.setProvinceName(province.getProvinceName());
            }
        }

        return response;
    }
    @Override
    public List<ComplexResponse> getComplexesByDistrictId(Integer districtId) {
        List<Complex> complexes = complexRepository.findByDistrict_DistrictId(districtId);
        return complexes.stream()
        		.filter(c -> c.getStatus() == ComplexStatus.active) // ✅ chỉ lấy sân đang hoạt động
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplexResponse> getComplexesByProvinceId(Integer provinceId) {
        List<Complex> complexes = complexRepository.findByDistrict_Province_ProvinceId(provinceId);
        return complexes.stream()
        		.filter(c -> c.getStatus() == ComplexStatus.active) // ✅ chỉ lấy sân đang hoạt động
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<ComplexResponse> searchComplexes(Integer provinceId, Integer districtId, PitchType pitchType) {
        List<Complex> complexes = complexRepository.findAll();
        
     // ✅ chỉ lấy sân đang hoạt động
        complexes = complexes.stream()
                .filter(c -> c.getStatus() == ComplexStatus.active)
                .collect(Collectors.toList());
        
        // Lọc theo provinceId nếu có
        if (provinceId != null) {
            complexes = complexes.stream()
                .filter(c -> c.getDistrict() != null && c.getDistrict().getProvince() != null 
                    && c.getDistrict().getProvince().getProvinceId().equals(provinceId))
                .collect(Collectors.toList());
        }

        // Lọc theo districtId nếu có
        if (districtId != null) {
            complexes = complexes.stream()
                .filter(c -> c.getDistrict() != null && c.getDistrict().getDistrictId().equals(districtId))
                .collect(Collectors.toList());
        }

        // Lọc theo pitchType nếu có
        if (pitchType != null) {
            complexes = complexes.stream()
                .filter(c -> c.getPitches() != null && c.getPitches().stream().anyMatch(p -> p.getType() == pitchType))
                .collect(Collectors.toList());
        }

        // Lọc theo date nếu có (sẽ được triển khai sau khi có endpoint /api/timeslots/available)
        // Hiện tại bỏ qua date để đảm bảo tìm kiếm hoạt động với các trường tùy chọn

        return complexes.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Override
    public ComplexResponse updateStatus(Integer id, String status) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        try {
            ComplexStatus newStatus = ComplexStatus.valueOf(status.toLowerCase());
            complex.setStatus(newStatus);
            return mapToResponse(complexRepository.save(complex));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }
    }
    
    @Override
    public List<ComplexResponse> getComplexesByOwner(Authentication authentication) {
        String email = authentication.getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return complexRepository.findByOwner_UserId(owner.getUserId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }   
    
    @Override
    public List<ComplexResponseDistance> findNearbyComplexes(double latitude, double longitude, double radiusKm) {
        List<Complex> complexes = complexRepository.findAll();

        return complexes.stream()
                // ✅ Chỉ lấy các cụm sân có tọa độ hợp lệ và đang active
                .filter(c -> 
                    c.getLatitude() != null && 
                    c.getLongitude() != null && 
                    c.getStatus() == ComplexStatus.active
                )
                .map(c -> {
                    double distance = calculateDistance(
                            latitude, longitude,
                            c.getLatitude().doubleValue(), c.getLongitude().doubleValue()
                    );

                    if (distance <= radiusKm) {
                        ComplexResponseDistance response = mapToResponseDistance(c);
                        response.setDistance(distance);
                        return response;
                    }
                    return null;
                })
                .filter(r -> r != null)
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance())) // Sắp xếp theo khoảng cách tăng dần
                .collect(Collectors.toList());
    }


    /**
     * ✅ Công thức Haversine để tính khoảng cách giữa 2 điểm lat/lng
     * Trả về đơn vị km
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Bán kính Trái Đất (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
    
    @Override
    public ComplexResponse updateAvatarCom(Integer id, MultipartFile file) {
        Complex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complex not found"));
        try {
            // Xoá file cũ
            if (complex.getAvatarCom() != null) {
                File oldFile = new File("." + complex.getAvatarCom());
                if (oldFile.exists()) oldFile.delete();
            }

            // Lưu file mới
            String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(avatarComDir, newFileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật path tương đối trong DB
            complex.setAvatarCom("/uploads/complexes/" + newFileName);
            complexRepository.save(complex);

            return mapToResponse(complex);
        } catch (IOException e) {
            throw new RuntimeException("Error saving avatar", e);
        }
    }
    
    private ComplexResponseDistance mapToResponseDistance(Complex complex) {
    	
    	String avatarComUrl = null;
        if (complex.getAvatarCom() != null && !complex.getAvatarCom().isEmpty()) {
        	avatarComUrl = serverUrl + complex.getAvatarCom();
        }
    	
        ComplexResponseDistance response = new ComplexResponseDistance();
        response.setId(complex.getComplexId());
        response.setName(complex.getName());
        response.setAddress(complex.getAddress());
        response.setLatitude(complex.getLatitude());
        response.setLongitude(complex.getLongitude());
        response.setPhone(complex.getPhone());
        response.setStatus(complex.getStatus());
        response.setCreatedAt(complex.getCreatedAt());
        response.setAvatarCom(avatarComUrl);

        if (complex.getOwner() != null) {
            response.setOwnerId(complex.getOwner().getUserId());
            response.setOwnerName(complex.getOwner().getFullName());
        }

        if (complex.getDistrict() != null) {
            response.setDistrictId(complex.getDistrict().getDistrictId());
            response.setDistrictName(complex.getDistrict().getDistrictName());
            if (complex.getDistrict().getProvince() != null) {
                Province province = complex.getDistrict().getProvince();
                response.setProvinceId(province.getProvinceId());
                response.setProvinceName(province.getProvinceName());
            }
        }

        return response;
    }
    
    

}
