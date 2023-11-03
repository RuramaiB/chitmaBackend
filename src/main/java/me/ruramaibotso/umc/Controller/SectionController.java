package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Section;
import me.ruramaibotso.umc.repository.SectionsRepository;
import me.ruramaibotso.umc.requests.SectionRequest;
import me.ruramaibotso.umc.services.SectionServices;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
@CrossOrigin
public class SectionController {
    private final SectionServices sectionServices;
    private final SectionsRepository sectionsRepository;

    @GetMapping("/getAllSections/{local}/{offset}")
    public Page<Section> getAllSections(@PathVariable String local, @PathVariable int offset ){
        return sectionServices.getSectionByLocal(offset, local);
    }
    @GetMapping("/getAllSection/{local}")
    public List<Section> getAllSections(@PathVariable String local){
        return sectionServices.getSectionByLocal(local);
    }
    @PostMapping
    public Section addNewSection(@RequestBody SectionRequest sectionRequest){
        return sectionServices.addNewSection(sectionRequest);
    }
    @GetMapping("/getSectionByName")
    public Section getSectionByName(String name){
        return sectionServices.getSectionByName(name);
    }
    @DeleteMapping("/deleteSectionByName/{name}")
    public String deleteSection(@PathVariable String name){
        Section section = sectionServices.getSectionByName(name);
        sectionsRepository.delete(section);
        return "Section Deleted Successfully";
    }
}
