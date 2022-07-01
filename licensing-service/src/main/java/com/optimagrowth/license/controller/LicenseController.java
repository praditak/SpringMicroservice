package com.optimagrowth.license.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/v1/organization/{organizationId}/license")
public class LicenseController {

	@Autowired
	LicenseService licenseService;

	/*
	 * The clientType parameter determines the type of Spring REST client to use.
	 */
	@RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.GET)
	public License getLicensesWithClient(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId, @PathVariable("clientType") String clientType) {
		return licenseService.getLicense(organizationId, licenseId, clientType);
	}

	@GetMapping(value = "/{licenseId}/{clientType}")
	public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {

		License license = licenseService.getLicense(licenseId, organizationId);

		/* HATEOS configuration */
		license.add(
				linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId()))
						.withSelfRel(),
				linkTo(methodOn(LicenseController.class).createLicense(organizationId, license))
						.withRel("createLicense"),
				linkTo(methodOn(LicenseController.class).updateLicense(organizationId, license))
						.withRel("updateLicense"),
				linkTo(methodOn(LicenseController.class).deleteLicense(organizationId, license.getLicenseId()))
						.withRel("deleteLicense"));

		/* HATEOS configuration end */

		return ResponseEntity.status(HttpStatus.OK).body(license);
	}

	@PutMapping
	public ResponseEntity<License> updateLicense(@PathVariable("organizationId") String organizationId,
			@RequestBody License request) {
		return ResponseEntity.ok(licenseService.updateLicense(request));
	}

	@PostMapping
	public ResponseEntity<License> createLicense(@PathVariable("organizationId") String organizationId,
			@RequestBody License request) {
		return ResponseEntity.ok(licenseService.createLicense(request, organizationId));
	}

	@DeleteMapping(value = "/{licenseId}")
	public ResponseEntity<String> deleteLicense(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
	}
}
