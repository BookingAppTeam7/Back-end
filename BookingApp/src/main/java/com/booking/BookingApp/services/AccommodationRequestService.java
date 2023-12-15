package com.booking.BookingApp.services;

import com.booking.BookingApp.models.accommodations.*;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationRequestPostDTO;
import com.booking.BookingApp.models.dtos.accommodations.AccommodationRequestPutDTO;
import com.booking.BookingApp.models.enums.AccommodationRequestStatus;
import com.booking.BookingApp.models.enums.AccommodationStatusEnum;
import com.booking.BookingApp.repositories.IAccommodationRepository;
import com.booking.BookingApp.repositories.IAccommodationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AccommodationRequestService implements IAccommodationRequestService{
    @Autowired
    public IAccommodationRequestRepository requestRepository;
    @Autowired
    public IAccommodationRepository accommodationRepository;
    @Override
    public List<AccommodationRequest> findAll() {
        return requestRepository.findAll();
    }

    @Override
    public Optional<AccommodationRequest> findById(Long id) {
        return requestRepository.findById(id);
    }

    @Override
    public List<AccommodationRequest> findByStatus(AccommodationRequestStatus status) {
        return requestRepository.findByRequestStatus(status);
    }

    @Override
    public Optional<AccommodationRequest> create(AccommodationRequestPostDTO newRequestDTO) throws Exception {
        AccommodationRequest newRequest=new AccommodationRequest(newRequestDTO.unapprovedAccommodationId,AccommodationRequestStatus.PENDING_CREATED,null);
        return Optional.of(requestRepository.save(newRequest));
    }

    @Override
    public Optional<AccommodationRequest> update(AccommodationRequestPutDTO updatedRequest, Long id) throws Exception {
        return Optional.empty();
    }

    @Override
    public Optional<AccommodationRequest> updateStatus(Long requestId, AccommodationRequestStatus status) {
        Optional<AccommodationRequest> request=requestRepository.findById(requestId);
        if(!request.isPresent()){
            return Optional.empty();
        }

        if(request.get().requestStatus==AccommodationRequestStatus.PENDING_CREATED){
            Optional<Accommodation> accommodation=accommodationRepository.findById(request.get().unapprovedAccommodationId);
            if(!accommodation.isPresent()){return  null;}

            if(status==AccommodationRequestStatus.APPROVED){
                //odobrio je novo kreirani smestaj
                //njegov id je request.unapprovedAccommodation i treba mu promeniti status
                accommodationRepository.updateStatus(request.get().unapprovedAccommodationId, AccommodationStatusEnum.APPROVED);
                int updatedRows=requestRepository.updateStatus(requestId, status);

                if (updatedRows > 0) {
                    return requestRepository.findById(requestId);
                }
            }
            else if(status==AccommodationRequestStatus.REJECTED){
                //treba ga obrisati iz baze i staviti status REJECTED
                accommodationRepository.updateStatus(request.get().unapprovedAccommodationId, AccommodationStatusEnum.BLOCKED);
                accommodationRepository.delete(accommodation.get());
                int updatedRows=requestRepository.updateStatus(requestId, status);

                if (updatedRows > 0) {
                    return requestRepository.findById(requestId);
                }
            }
            else{  //menja se status u PENDING_CREATED ili PENDING_EDITED ili PENDING_DELETED
                return Optional.empty();
            }
        }


        else if(request.get().requestStatus==AccommodationRequestStatus.PENDING_EDITED){
            Optional<Accommodation> accommodation=accommodationRepository.findById(request.get().unapprovedAccommodationId);
            if(!accommodation.isPresent()){return  null;}

            if(status==AccommodationRequestStatus.APPROVED){
                //odobrio je izmenu za smestaj
                //njegov id je request.originalId i treba mu promeniti status
                //treba primeniti izmene
                Optional<Accommodation> originalAccommodation=accommodationRepository.findById(request.get().originalAccommodationId);

                accommodationRepository.delete(accommodation.get());//treba obrisati taj objekat koji je sluzio samo za zamenu zbog zajednickih referenci
                Accommodation updatedAccommodation=new Accommodation(request.get().originalAccommodationId, accommodation.get().name, accommodation.get().description,accommodation.get().location,accommodation.get().minGuests,accommodation.get().maxGuests,accommodation.get().type,accommodation.get().assets,originalAccommodation.get().prices,accommodation.get().ownerId, AccommodationStatusEnum.APPROVED,accommodation.get().cancellationDeadline,accommodation.get().reservationConfirmation, originalAccommodation.get().reviews,accommodation.get().images, false);
                accommodationRepository.saveAndFlush(updatedAccommodation);
                int updatedRows=requestRepository.updateStatus(requestId, status);

                if (updatedRows > 0) {
                    return requestRepository.findById(requestId);
                }
            }
            else if(status==AccommodationRequestStatus.REJECTED){
                //treba sacuvati stare izmene i obrisati taj unapproved objekat
                accommodationRepository.updateStatus(request.get().unapprovedAccommodationId, AccommodationStatusEnum.BLOCKED);
                accommodationRepository.delete(accommodation.get()); //brisemo taj izmenjeni objekat
                int updatedRows=requestRepository.updateStatus(requestId, status);

                if (updatedRows > 0) {
                    return requestRepository.findById(requestId);
                }
            }
            else{  //menja se status u PENDING_CREATED ili PENDING_EDITED ili PENDING_EDITED
                return Optional.empty();
            }

        }

//        int updatedRows=requestRepository.updateStatus(requestId, status);
//
//        if (updatedRows > 0) {
//            return requestRepository.findById(requestId);
//        }
        return Optional.empty();
    }

    @Override
    public List<AccommodationRequest> findByRequestStatus(AccommodationRequestStatus status1, AccommodationRequestStatus status2) {
        return requestRepository.findByRequestStatusIn(Arrays.asList(status1, status2));
    }
}
