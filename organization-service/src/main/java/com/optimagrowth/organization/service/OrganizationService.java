package com.optimagrowth.organization.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;

	public Organization findById(String organizationId) {
		Optional<Organization> opt = organizationRepository.findById(organizationId);

		return (opt.isPresent()) ? opt.get() : null;
	}

	public Organization create(Organization organization) {
		organization.setId(UUID.randomUUID().toString());
		organization = organizationRepository.save(organization);
		return organization;
	}

	public void update(Organization organization) {
		organizationRepository.save(organization);
	}

	public void delete(Organization organization) {
		organizationRepository.deleteById(organization.getId());
	}
}
