package com.optimagrowth.license.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;

@Service
public class LicenseService {

	@Autowired
	MessageSource messages;

	@Autowired
	LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;
	
	
	@Autowired
	OrganizationFeignClient organizationFeignClient;

	@Autowired
	OrganizationRestTemplateClient organizationRestClient;

	@Autowired
	OrganizationDiscoveryClient organizationDiscoveryClient;

	public License getLicense(String licenseId, String organizationId) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if (null == license) {
			throw new IllegalArgumentException(String.format(
					messages.getMessage("license.search.error.message", null, null), licenseId, organizationId));
		}

		return license.withComment(config.getProperty());

	}

	public License createLicense(License license, String organizationId) {
		license.setLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);
		// String responseMessage = null;
		/*
		 * if (license != null) { license.setOrganizationId(organizationId);
		 * responseMessage = String.format("This is the post and the object is: %s",
		 * license.toString()); }
		 */
		return license.withComment(config.getProperty());
	}

	public License updateLicense(License license) {
		/*
		 * String responseMessage = null; if (license != null) {
		 * license.setOrganizationId(organizationId); responseMessage =
		 * String.format("This is the Put and the object is: %s", license.toString()); }
		 * return responseMessage;
		 */

		licenseRepository.save(license);
		return license.withComment(config.getProperty());
	}

	public String deleteLicense(String licenseId) {
		String responseMessage = null;
		License license = new License();
		license.setLicenseId(licenseId);

		licenseRepository.delete(license);

		responseMessage = String.format("Deleting license with id %s ", licenseId);
		return responseMessage;
	}

	public License getLicense(String organizationId, String licenseId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

		if (null == license) {
			throw new IllegalArgumentException(String.format(
					messages.getMessage("license.search.error.message", null, null), licenseId, organizationId));
		}

		Organization organization = retrieveOrganizationInfo(organizationId, clientType);
		if (null != organization) {
			license.setOrganizationName(organization.getName());
			license.setContactName(organization.getContactName());
			license.setContactEmail(organization.getContactEmail());
			license.setContactPhone(organization.getContactPhone());
		}
		return license.withComment(config.getExampleProperty());
	}

	private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
		Organization organization = null;

		switch (clientType) {
		case "feign":
			System.out.println("I am using the feign client");
			organization = organizationFeignClient.getOrganization(organizationId);
			break;
		case "rest":
			System.out.println("I am using the rest client");
			organization = organizationRestClient.getOrganization(organizationId);
			break;
		case "discovery":
			System.out.println("I am using the discovery client");
			organization = organizationDiscoveryClient.getOrganization(organizationId);
			break;
		default:
			organization = organizationRestClient.getOrganization(organizationId);
			break;
		}

		return organization;
	}

}
