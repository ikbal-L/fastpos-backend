package com.softlines.fastpos.jwtsecurity.jwtcontroller;


import com.softlines.fastpos.jwtsecurity.securitydomain.Terminal;
import com.softlines.fastpos.dto.TerminalDto;
import com.softlines.fastpos.dto.mapping.TerminalMapper;
import com.softlines.fastpos.jwtsecurity.securityrepository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    private TerminalRepository terminalRepository;

    @PostMapping("/save")
    public ResponseEntity addTerminal(@RequestBody TerminalDto terminalDto) {

        try {

            Optional<Terminal> optionalTerminal = terminalRepository.findById(terminalDto.getId());

            if (!optionalTerminal.isPresent()) {
               Terminal terminal= terminalRepository.save(terminalMapper.toTerminal(terminalDto) );
                return ResponseEntity.status(HttpStatus.CREATED).body(terminalMapper.toTerminalDto(terminal));
            } else {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<TerminalDto>> getTerminals() {
        try {
            List<Terminal> terminales = terminalRepository.findAll();
            if (terminales != null)
                return ResponseEntity.ok().body(terminalMapper.toTerminalDTOs(terminales));
            else
                return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TerminalDto> getTerminal(@PathVariable long id) {

        try {
            Optional<Terminal> optionalterminal = terminalRepository.findById(id);

            if (optionalterminal.isPresent())
                return ResponseEntity.ok().body(terminalMapper.toTerminalDto(optionalterminal.get()));
            else
                return ResponseEntity.notFound().build();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }

    @PutMapping("/put/{id}")
    public ResponseEntity editTerminal(@PathVariable long id, @RequestBody TerminalDto terminalDto) {

        try {
            Optional<Terminal> optionalTerminal = terminalRepository.findById(id);

            if (optionalTerminal.isPresent()) {
                Terminal terminal = terminalMapper.toTerminal(terminalDto);
                return ResponseEntity.ok().body(terminalMapper.toTerminalDto(terminalRepository.save(terminal)));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTerminal(@PathVariable long id) {

        try {

            Optional<Terminal> optionalterminal = terminalRepository.findById(id);
            if (optionalterminal.isPresent()) {

                terminalRepository.delete(optionalterminal.get());
                return ResponseEntity.ok().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not Found", exception);
        }
    }
}
