package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.OrganisationLeadership;
import me.ruramaibotso.umc.model.Organisations;
import me.ruramaibotso.umc.repository.OrganisationsRepository;
import me.ruramaibotso.umc.requests.OrganisationsRequest;
import me.ruramaibotso.umc.services.OrganisationServices;
import me.ruramaibotso.umc.user.Organisation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/organisations")
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class OrganisationsController {
    private final OrganisationServices organisationServices;
    private final OrganisationsRepository organisationsRepository;

    @GetMapping("/getAllOrganisations/{local}")
    public List<Organisations> getAllOrganisations(@PathVariable String local){
        return organisationServices.getAllOrganisations(local);
    }
    @PostMapping
    public Organisations addOrganisation(@RequestBody OrganisationsRequest organisationsRequest){
        return organisationServices.addOrganisation(organisationsRequest);
    }
    @PutMapping
    public Organisations updateOrganisation(Integer ID, OrganisationsRequest organisationsRequest){
        Organisations organisations = organisationsRepository.findById(ID)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation not found"));
        organisations.setOrganisation(organisationsRequest.getOrganisation());
        organisations.setOrganisation(organisationsRequest.getOrganisation());
        return organisationsRepository.save(organisations);
    }
    @GetMapping("/getOrganisationByID/{ID}")
    public Organisations getOrganisationByID(@PathVariable Integer ID){
        return organisationsRepository.findById(ID)
                .orElseThrow(()-> new ResourceNotFoundException("Organisation not found."));
    }
    @DeleteMapping("/deleteOrganisationByID/{ID}")
    public String deleteOrganisation(@PathVariable Integer ID){
        Organisations organisations = organisationsRepository.findById(ID)
                        .orElseThrow(()-> new ResourceNotFoundException("Organisation not found"));
        organisationsRepository.delete(organisations);
        return "Organisation deleted successfully.";
    }
}
