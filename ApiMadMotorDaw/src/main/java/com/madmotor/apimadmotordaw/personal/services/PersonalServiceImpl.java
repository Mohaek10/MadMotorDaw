/*


import com.madmotor.apimadmotordaw.personal.dto.PersonalCreateDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalResponseDTO;
import com.madmotor.apimadmotordaw.personal.dto.PersonalUpdateDTO;
import com.madmotor.apimadmotordaw.personal.exceptions.PersonalNotFound;
import com.madmotor.apimadmotordaw.personal.mappers.PersonalMapper;
import com.madmotor.apimadmotordaw.personal.repositories.PersonalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "personal")
@Slf4j

public class PersonalServiceImpl implements PersonalService {
    private final PersonalRepository personalRepository;
    private final PersonalMapper personalMapper;
    //private final ObjectMapper mapper;

    @Autowired


    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper personalMapper) {
        this.personalRepository = personalRepository;
        this.personalMapper = personalMapper;
    }


    @Override
    public Page<PersonalResponseDTO> findAll(Optional<String> dni, Optional<String> nombre, Optional<String> apellidos, Optional<LocalDate> fechaNacimiento, Optional<String> direccion, Optional<String> iban, Pageable pageable) {
        return null;
    }

    @Override
    public PersonalResponseDTO findById(Long id) {

        return personalMapper.toPersonalResponseDto(personalRepository.findById(id).orElseThrow(() -> new PersonalNotFound("Trabajador@ no encontrado")));
    }

    @Override
    public PersonalResponseDTO save(PersonalCreateDTO personalCreateDto) {

        return personalMapper.toPersonalResponseDto(personalRepository.save(personalMapper.toPersonal(personalCreateDto)));
    }

    @Override
    public PersonalResponseDTO update(Long id, PersonalUpdateDTO personalUpdateDto) {
        var personalActualizar= personalRepository.findById(id).orElseThrow(() -> new PersonalNotFound("Trabajador@ no encontrado"));
        personalActualizar.setDireccion(personalUpdateDto.getDireccion());
        personalActualizar.setIban(personalUpdateDto.getIban());

        var personalActualizado = personalRepository.save(personalActualizar);
        return personalMapper.toPersonalResponseDto(personalActualizado);
    }

    @Override
    public void deleteById(Long id) {

    }

}

 */
