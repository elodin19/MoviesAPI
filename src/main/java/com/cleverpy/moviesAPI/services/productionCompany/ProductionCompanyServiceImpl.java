package com.cleverpy.moviesAPI.services.productionCompany;

import com.cleverpy.moviesAPI.dto.ProductionCompanyDto;
import com.cleverpy.moviesAPI.entities.ProductionCompany;
import com.cleverpy.moviesAPI.repositories.ProductionCompanyRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the Production Company Service
 */
@Service
public class ProductionCompanyServiceImpl implements ProductionCompanyService {

    @Autowired
    private ProductionCompanyRepository companyRepository;

    public ProductionCompanyServiceImpl(ProductionCompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Method to create a new Production Company
     * Tests if name or pathLogo aren't being used yet
     * @param companyDto
     * @return ResponseEntity (ok: ProductionCompany, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> create(ProductionCompanyDto companyDto) {

        //Tests if the company name already exists
        if (companyRepository.existsByName(companyDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The production company " + companyDto.getName() + " is already registered"));

        //Tests if the logo path already exists
        if (companyRepository.existsByLogoPath(companyDto.getLogo_path()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The logo path " + companyDto.getLogo_path() + " is already registered"));

        //Creates and saves the new company
        ProductionCompany company = new ProductionCompany(null, companyDto.getName(), companyDto.getLogo_path(),
                companyDto.getOrigin_country());
        companyRepository.save(company);

        return ResponseEntity.ok(company);
    }

    /**
     * Method to get the Production Company data
     * @param id
     * @return ResponseEntity (ok: ProductionCompany, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        //Gets the company
        Optional<ProductionCompany> companyOpt = companyRepository.findById(id);

        return ResponseEntity.ok(companyOpt.get());
    }

    /**
     * Method to get all Production Companies
     * @return ResponseEntity (Page<ProductionCompany>, no content)
     */
    @Override
    public ResponseEntity<?> getAll(Integer pageNumber) {

        Pageable page = PageRequest.of(pageNumber, 10);
        Page<ProductionCompany> companies = companyRepository.findAll(page);

        if (companies.toList().size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(companies);
    }

    /**
     * Method to update the Production Company
     * Tests if name or logoPath aren't being used yet
     * @param id
     * @param companyDto
     * @return ResponseEntity (ok: ProductionCompany, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> update(Long id, ProductionCompanyDto companyDto) {

        //Gets the company
        Optional<ProductionCompany> companyOpt = companyRepository.findById(id);

        //Tests if the name or the logo path already exists
        if (companyRepository.existsByName(companyDto.getName()) && !companyOpt.get().getName().equalsIgnoreCase(companyDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The production company " + companyDto.getName() + " is already registered"));

        if (companyRepository.existsByLogoPath(companyDto.getLogo_path()) && !companyOpt.get().getLogoPath().equalsIgnoreCase(companyDto.getLogo_path()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The logo path " + companyDto.getLogo_path() + " is already registered"));

        //Updates and saves the company
        companyOpt.get().setName(companyDto.getName());
        companyOpt.get().setLogoPath(companyDto.getLogo_path());
        companyOpt.get().setOriginCountry(companyDto.getOrigin_country());
        companyRepository.save(companyOpt.get());

        return ResponseEntity.ok(companyOpt.get());

    }

    /**
     * Method to delete a Production Company
     * @param id
     * @return ResponseEntity(MessageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        //Gets the company
        Optional<ProductionCompany> companyOpt = companyRepository.findById(id);

        companyRepository.delete(companyOpt.get());
        return ResponseEntity.ok(new MessageResponse("Production Company " + id + " deleted with success"));
    }
}
