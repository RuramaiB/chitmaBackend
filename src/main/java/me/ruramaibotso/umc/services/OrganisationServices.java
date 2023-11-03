package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Organisations;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.repository.OrganisationsRepository;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.requests.OrganisationsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationServices {

    private final OrganisationsRepository organisationsRepository;
    private final LocalsRepository localsRepository;
    public List<Organisations> getAllOrganisations( String local){
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Preaching point not found"));
        return organisationsRepository.findAllByLocals(locals);
    }
    public Organisations addOrganisation(OrganisationsRequest organisationsRequest){
        Organisations organisations = new Organisations();
        Locals locals = localsRepository.findByName(organisationsRequest.getPreachingPoint())
                .orElseThrow(() -> new ResourceNotFoundException("Preaching point not found"));
        organisations.setId(organisations.getId());
        organisations.setOrganisation(organisationsRequest.getOrganisation());
        organisations.setLocals(locals);
        return organisationsRepository.save(organisations);
    }



}
