package me.ruramaibotso.umc.services;

import lombok.RequiredArgsConstructor;
import me.ruramaibotso.umc.exception.ResourceNotFoundException;
import me.ruramaibotso.umc.model.Locals;
import me.ruramaibotso.umc.repository.LocalsRepository;
import me.ruramaibotso.umc.requests.LocalsRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalsService {

    private final LocalsRepository localsRepository;

    public List<Locals> getAllPreachingPoints(){
        return localsRepository.findAll();
    }
    public Locals getPreachingPointByName(String name){
        return localsRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(("Local does not exist")));
    }
    public Locals addNewPreachingPoint(LocalsRequest localsRequest){
        Locals locals = new Locals();
        locals.setId(locals.getId());
        locals.setPrefix(localsRequest.getPrefix());
        locals.setName(localsRequest.getLocalName());
        locals.setLocation(localsRequest.getLocalLocation());
        return localsRepository.save(locals);
    }
}
