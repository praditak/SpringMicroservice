package com.optimagrowth.license.repository;

import org.springframework.data.repository.CrudRepository;

import com.optimagrowth.license.model.License;

public interface LicenseRepository extends CrudRepository<License, String>{

	License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

}
