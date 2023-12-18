package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.PermissionDto;
import com.my.hotel.entity.Permission;
import com.my.hotel.repo.PermissionRepo;
import com.my.hotel.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepo repo;

	@Override
	public List<PermissionDto> permissions() {
		List<PermissionDto> ret = new ArrayList<>();
		List<Permission> permissions = this.repo.findAll();
		if (!permissions.isEmpty()) {
			ret = ObjectMapperUtils.mapAll(permissions, PermissionDto.class);
		}
		return ret;
	}

}
