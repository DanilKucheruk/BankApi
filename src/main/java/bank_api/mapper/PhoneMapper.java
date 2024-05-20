package bank_api.mapper;

import org.springframework.stereotype.Component;

import bank_api.dto.PhoneDto;
import bank_api.entity.Phone;

@Component
public class PhoneMapper implements Mapper<Phone, PhoneDto> {

    @Override
    public PhoneDto map(Phone phone) {
        if (phone == null) {
            return null;
        }
        PhoneDto phoneDto = new PhoneDto();
        phoneDto.setId(phone.getId());
        phoneDto.setPhoneNumber(phone.getPhoneNumber());
        phoneDto.setClientId(phone.getClient() != null ? phone.getClient().getId() : null);
        return phoneDto;
    }

}
