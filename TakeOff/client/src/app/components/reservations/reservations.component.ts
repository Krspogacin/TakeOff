import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { AppComponent } from 'src/app/app.component';

enum ReservationState {
  PASSED,
  IN_PROGRESS,
  ACTIVE,
}

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {

  loading = true;
  exists = false;
  reservations: any[] = [];

  constructor(private authService: AuthenticationService,
              private reservationService: ReservationService,
              private appComponent: AppComponent) { }

  ngOnInit() {
    const username = this.authService.getUsername();
    if (username) {
      this.gatherReservationsData(username);
    }
    this.reservationService.reservationsSubject.subscribe(
      () => {
        this.gatherReservationsData(username);
      }
    );
  }

  gatherReservationsData(username: string) {
    if (!username) {
      return;
    }
    this.reservationService.getReservations(username).subscribe(
      (data: []) => {
        this.reservations = data;
        this.loading = false;
        this.exists = data.length > 0;

        this.gatherFlightReservationsData(data);
        this.gatherVehicleReservationsData(data);
        this.gatherRoomReservationsData(data);
      },
      () => {
        this.loading = false;
        this.appComponent.showSnackBar('Error! Could not load reservation history!');
      }
    );
  }

  gatherFlightReservationsData(reservations: any[]) {
    if (reservations) {
      reservations.forEach((reservation, index) => {

        const currentDate = new Date();
        const takeOffDate = new Date(reservation.ticket.flight.takeOffDate);
        const landingDate = new Date(reservation.ticket.flight.landingDate);
        if (takeOffDate.getTime() <= currentDate.getTime() && landingDate.getTime() <= currentDate.getTime()) {
          reservation.state = ReservationState.PASSED;
        } else if (takeOffDate.getTime() <= currentDate.getTime() && landingDate.getTime() > currentDate.getTime()) {
          reservation.state = ReservationState.IN_PROGRESS;
        } else {
          reservation.state = ReservationState.ACTIVE;
        }
        const threeHoursBeforeTakeOff = new Date(takeOffDate);
        threeHoursBeforeTakeOff.setHours(takeOffDate.getHours() - 3);
        if (reservation.state === ReservationState.ACTIVE && currentDate.getTime() <= threeHoursBeforeTakeOff.getTime()) {
          reservation.canCancel = true;
        } else {
          reservation.canCancel = false;
        }

        reservation.aircompanyRatingToShow = reservation.aircompanyRating;
        reservation.flightRatingToShow = reservation.flightRating;
        this.reservations[index] = reservation;
      });
    }
  }

  gatherVehicleReservationsData(reservations: any[]) {
    if (reservations) {
      reservations.forEach((reservation, index) => {
        if (reservation.reservationDTO.vehicleReservation) {
          const currentDate = new Date();
          const startDate = new Date(reservation.reservationDTO.vehicleReservation.reservationStartDate);
          const endDate = new Date(reservation.reservationDTO.vehicleReservation.reservationEndDate);
          if (startDate.getTime() <= currentDate.getTime() && endDate.getTime() <= currentDate.getTime()) {
            reservation.reservationDTO.vehicleReservation.state = ReservationState.PASSED;
          } else if (startDate.getTime() <= currentDate.getTime() && endDate.getTime() > currentDate.getTime()) {
            reservation.reservationDTO.vehicleReservation.state = ReservationState.IN_PROGRESS;
          } else {
            reservation.reservationDTO.vehicleReservation.state = ReservationState.ACTIVE;
          }
          const twoDaysBefore = new Date(startDate);
          twoDaysBefore.setDate(startDate.getDate() - 2);
          if (reservation.reservationDTO.vehicleReservation.state === ReservationState.ACTIVE &&
              currentDate.getTime() <= twoDaysBefore.getTime()) {

            reservation.reservationDTO.vehicleReservation.canCancel = true;
          } else {
            reservation.reservationDTO.vehicleReservation.canCancel = false;
          }

          reservation.reservationDTO.vehicleReservation.rentACarRatingToShow = reservation.reservationDTO.vehicleReservation.rentACarRating;
          reservation.reservationDTO.vehicleReservation.vehicleRatingToShow = reservation.reservationDTO.vehicleReservation.vehicleRating;

          this.reservations[index].reservationDTO.vehicleReservation = reservation.reservationDTO.vehicleReservation;
        }
      });
    }
  }

  gatherRoomReservationsData(reservations: any[]) {
    if (reservations) {
      reservations.forEach((reservation, index) => {
        if (reservation.reservationDTO.roomReservation) {
          const currentDate = new Date();
          const startDate = new Date(reservation.reservationDTO.roomReservation.reservationStartDate);
          const endDate = new Date(reservation.reservationDTO.roomReservation.reservationEndDate);
          if (startDate.getTime() <= currentDate.getTime() && endDate.getTime() <= currentDate.getTime()) {
            reservation.reservationDTO.roomReservation.state = ReservationState.PASSED;
          } else if (startDate.getTime() <= currentDate.getTime() && endDate.getTime() > currentDate.getTime()) {
            reservation.reservationDTO.roomReservation.state = ReservationState.IN_PROGRESS;
          } else {
            reservation.reservationDTO.roomReservation.state = ReservationState.ACTIVE;
          }
          const twoDaysBefore = new Date(startDate);
          twoDaysBefore.setDate(startDate.getDate() - 2);
          if (reservation.reservationDTO.roomReservation.state === ReservationState.ACTIVE &&
              currentDate.getTime() <= twoDaysBefore.getTime()) {

            reservation.reservationDTO.roomReservation.canCancel = true;
          } else {
            reservation.reservationDTO.roomReservation.canCancel = false;
          }

          reservation.reservationDTO.roomReservation.hotelRatingToShow = reservation.reservationDTO.roomReservation.hotelRating;
          // rooms ratings
          const roomsAndRatings = reservation.reservationDTO.roomReservation.roomsAndRatings;
          const flags = [];
          for (let i = 0; i < roomsAndRatings.length; i++) {
            if (flags[roomsAndRatings[i].room.id]) {
              roomsAndRatings[i].show = false;
              continue;
            }
            flags[roomsAndRatings[i].room.id] = true;
            roomsAndRatings[i].ratingToShow = roomsAndRatings[i].rating;
            roomsAndRatings[i].show = true;
          }
          reservation.reservationDTO.roomReservation.roomsAndRatings = roomsAndRatings;
          this.reservations[index].reservationDTO.roomReservation = reservation.reservationDTO.roomReservation;
        }
      });
    }
  }

  rateAirCompany(index: number, rating: number) {
    const ratingReservation = this.reservations[index];
    const aircompanyRating = {'username': this.authService.getUsername(),
                              'rating' : rating,
                              'airCompany' : ratingReservation.ticket.flight.company};
    this.reservationService.rateAirCompany(aircompanyRating).subscribe(
        () => {
            this.reservations[index].aircompanyRating = rating;
            this.reservations[index].aircompanyRatingToShow = rating;

            this.reservations.forEach(reservation => {
                if (ratingReservation.ticket.flight.company.id === reservation.ticket.flight.company.id) {
                    reservation.aircompanyRating = rating;
                    reservation.aircompanyRatingToShow = rating;
                }
            });

            this.appComponent.showSnackBar('You have rated air company successfully!');
        },
        () => {
            this.appComponent.showSnackBar('Error! Something went wrong');
        }
    );
  }

  rateFlight(index: number, rating: number) {
    const ratingReservation = this.reservations[index];
    const flightRating = {'username': this.authService.getUsername(),
                          'rating' : rating,
                          'flight' : ratingReservation.ticket.flight};
    this.reservationService.rateFlight(flightRating).subscribe(
        () => {
            this.reservations[index].flightRating = rating;
            this.reservations[index].flightRatingToShow = rating;
            this.reservations.forEach(reservation => {
                if (ratingReservation.ticket.flight.id === reservation.ticket.flight.id) {
                    reservation.flightRating = rating;
                    reservation.flightRatingToShow = rating;
                }
            });
            this.appComponent.showSnackBar('You have rated flight successfully!');
        },
        () => {
            this.appComponent.showSnackBar('Error! Something went wrong');
        }
    );
  }

  rateRentACar(index: number, rating: number) {
    const ratingReservation = this.reservations[index];
    const rentACarRating = {'username': this.authService.getUsername(),
                            'rating' : rating,
                            'rentACar' : ratingReservation.reservationDTO.vehicleReservation.vehicle.rentACar};
    this.reservationService.rateRentACar(rentACarRating).subscribe(
        () => {
            this.reservations[index].reservationDTO.vehicleReservation.rentACarRating = rating;
            this.reservations[index].reservationDTO.vehicleReservation.rentACarRatingToShow = rating;

            this.reservations.forEach(reservation => {
                if (reservation.reservationDTO.vehicleReservation &&
                    ratingReservation.reservationDTO.vehicleReservation.vehicle.rentACar.id ===
                    reservation.reservationDTO.vehicleReservation.vehicle.rentACar.id) {

                    reservation.reservationDTO.vehicleReservation.rentACarRating = rating;
                    reservation.reservationDTO.vehicleReservation.rentACarRatingToShow = rating;
                }
            });
            this.appComponent.showSnackBar('You have rated rent a car successfully!');
        },
        () => {
            this.appComponent.showSnackBar('Error! Something went wrong');
        }
    );
  }

  rateVehicle(index: number, rating: number) {
    const ratingReservation = this.reservations[index];
    const vehicleRating = {'username': this.authService.getUsername(),
                            'rating' : rating,
                            'vehicle' : ratingReservation.reservationDTO.vehicleReservation.vehicle};
    this.reservationService.rateVehicle(vehicleRating).subscribe(
        () => {
            this.reservations[index].reservationDTO.vehicleReservation.vehicleRating = rating;
            this.reservations[index].reservationDTO.vehicleReservation.vehicleRatingToShow = rating;

            this.reservations.forEach(reservation => {
            if (reservation.reservationDTO.vehicleReservation &&
                ratingReservation.reservationDTO.vehicleReservation.vehicle.id ===
                reservation.reservationDTO.vehicleReservation.vehicle.id) {

                reservation.reservationDTO.vehicleReservation.vehicleRating = rating;
                reservation.reservationDTO.vehicleReservation.vehicleRatingToShow = rating;
            }
            });
            this.appComponent.showSnackBar('You have rated vehicle successfully!');
        },
        () => {
            this.appComponent.showSnackBar('Error! Something went wrong');
        }
    );
  }

  rateHotel(index: number, rating: number) {
    const ratingReservation = this.reservations[index];
    const hotelRating = {'username': this.authService.getUsername(),
                         'rating' : rating,
                         'hotel' : ratingReservation.reservationDTO.roomReservation.roomsAndRatings[0].room.hotel};
    this.reservationService.rateHotel(hotelRating).subscribe(
        () => {
            this.reservations[index].reservationDTO.roomReservation.hotelRating = rating;
            this.reservations[index].reservationDTO.roomReservation.hotelRatingToShow = rating;

            this.reservations.forEach(reservation => {
            if (reservation.reservationDTO.roomReservation &&
                ratingReservation.reservationDTO.roomReservation.roomsAndRatings[0].room.hotel.id ===
                reservation.reservationDTO.roomReservation.roomsAndRatings[0].room.hotel.id) {

                reservation.reservationDTO.roomReservation.hotelRating = rating;
                reservation.reservationDTO.roomReservation.hotelRatingToShow = rating;
            }
            });

            this.appComponent.showSnackBar('You have rated hotel successfully!');
        },
        () => {
            this.appComponent.showSnackBar('Error! Something went wrong');
        }
    );
  }

  rateRoom(index: number, roomIndex: number, rating: number) {
    const ratingReservation = this.reservations[index];
    const roomRating = {'username': this.authService.getUsername(),
                        'rating' : rating,
                        'room' : ratingReservation.reservationDTO.roomReservation.roomsAndRatings[roomIndex].room};
    this.reservationService.rateRoom(roomRating).subscribe(
        () => {
            this.reservations[index].reservationDTO.roomReservation.roomsAndRatings[roomIndex].rating = rating;
            this.reservations[index].reservationDTO.roomReservation.roomsAndRatings[roomIndex].ratingToShow = rating;

            this.reservations.forEach(reservation => {
                if (reservation.reservationDTO.roomReservation) {
                    reservation.reservationDTO.roomReservation.roomsAndRatings.forEach((roomAndRating, i) => {
                        if (ratingReservation.reservationDTO.roomReservation.roomsAndRatings[roomIndex].room.id === roomAndRating.room.id) {

                            reservation.reservationDTO.roomReservation.roomsAndRatings[i].rating = rating;
                            reservation.reservationDTO.roomReservation.roomsAndRatings[i].ratingToShow = rating;
                        }
                    });
                }
            });
            this.appComponent.showSnackBar('You have rated room successfully!');
        },
        () => {
            this.appComponent.showSnackBar('Error! Something went wrong');
        }
    );
  }

  cancelFlightReservation(index: number) {

  }

  cancelVehicleReservation(index: number) {

  }

  cancelRoomReservation(index: number) {

  }
}
