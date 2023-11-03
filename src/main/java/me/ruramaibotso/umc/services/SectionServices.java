package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.model.Section;
import me.ruramaibotso.umc.model.SectionFinance;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.repository.SectionsRepository;
import me.ruramaibotso.umc.requests.SectionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServices {
    private final SectionsRepository sectionsRepository;
    private final LocalsRepository localsRepository;

    public List<me.ruramaibotso.umc.model.Section> getAllSections(){
        return sectionsRepository.findAll();
    }
    public Section addNewSection(SectionRequest sectionRequest){
        Locals locals = localsRepository.findByName(sectionRequest.getPreachingPoint())
                .orElseThrow(()-> new RuntimeException("Preaching point not found !"));
        Section section = new Section();
        section.setId(section.getId());
        section.setName(sectionRequest.getSectionName());
        section.setLocals(locals);
        return sectionsRepository.save(section);
    }

    public Section getSectionByName(String name){
        return sectionsRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(("Section does not exist")));
    }

    public Page<Section> getSectionByLocal(int offset, String local) {
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        return sectionsRepository.findAllByLocals(PageRequest.of(offset,10), locals);
    }
    public List<Section> getSectionByLocal(String local) {
        Locals locals = localsRepository.findByName(local)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found."));
        return sectionsRepository.findAllByLocals(locals);
    }

}
