package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.SubscriberDTOForAdd;
import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.entity.Subscriber;

import java.util.List;

public class SubscriberMapper {
    public Subscriber toEntityForAdd(SubscriberDTOForAdd dto) {
        Subscriber entity = new Subscriber();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public SubscriberDTOForView toDTOForView(Subscriber subscriber) {
        return SubscriberDTOForView.builder()
                .id(subscriber.getId())
                .email(subscriber.getEmail())
                .build();
    }

    public List<SubscriberDTOForView> toDTOForView(List<Subscriber> subscribers) {
        return subscribers.stream()
                .map(val -> SubscriberDTOForView.builder()
                        .id(val.getId())
                        .email(val.getEmail())
                        .build())
                .toList();
    }
}
