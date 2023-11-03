package me.ruramaibotso.umc.Controller;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.requests.LocalsRequest;
import me.ruramaibotso.umc.services.LocalsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/local")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class LocalsController {

    private final LocalsService localsService;
    private final LocalsRepository localsRepository;

    @GetMapping("/getAllLocalPreachingPoints")
    public List<Locals> getAllPreachingPoints(){
        return localsService.getAllPreachingPoints();
    }
    @PostMapping
    public Locals addNewPreachingPoint(@RequestBody LocalsRequest localsRequest){
        return localsService.addNewPreachingPoint(localsRequest);
    }
    @GetMapping("/getLocalPreachingPointByName/")
    public Locals getPreachingPointByName(String preachingPoint){
        return localsService.getPreachingPointByName(preachingPoint);
    }

    @PutMapping("/updateLocalByName/{localName}")
    public Locals updateLocalByName(@PathVariable String localName, @RequestBody LocalsRequest localsRequest){
        Locals locals = localsRepository.findByName(localName)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
        locals.setName(localsRequest.getLocalName());
        locals.setLocation(localsRequest.getLocalLocation());
        return localsRepository.save(locals);
    }
    @DeleteMapping("/deleteLocalPreachingPointByName/{preachingPointName}")
    public String deletePreachingPoint(@PathVariable String preachingPointName){
        Locals locals = localsService.getPreachingPointByName(preachingPointName);
        localsRepository.delete(locals);
        return "Delete local by name successful.";
    }
}
