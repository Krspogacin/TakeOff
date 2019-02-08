import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { AppComponent } from 'src/app/app.component';
import { AmChartsService, AmChart } from '@amcharts/amcharts3-angular';

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
export class ReservationsComponent implements OnInit, AfterViewInit, OnDestroy {


  loading = true;
  exists = false;
  reservations: any[] = [];

  private chart: AmChart;

  constructor(private authService: AuthenticationService,
              private reservationService: ReservationService,
              private appComponent: AppComponent,
              private amCharts: AmChartsService) { }

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

    // this.chart = this.amCharts.makeChart('chartdiv', {'type': 'serial',
    //                                                   'theme': 'light',
    //                                                   'dataProvider': []
    // });
  }

  ngAfterViewInit() {
    // setTimeout(() => this.charts(), 3000);
  }

  ngOnDestroy() {
    // if (this.chart) {
    //   this.amCharts.destroyChart(this.chart);
    // }
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

  charts() {
    this.chart = this.amCharts.makeChart('chartdiv', {
        "type": "serial",
        "theme": "light",
        "marginRight": 40,
        "marginLeft": 40,
        "autoMarginOffset": 20,
        "mouseWheelZoomEnabled":true,
        "dataDateFormat": "YYYY-MM-DD",
        "valueAxes": [{
            "id": "v1",
            "axisAlpha": 0,
            "position": "left",
            "ignoreAxisWidth":true
        }],
        "balloon": {
            "borderThickness": 1,
            "shadowAlpha": 0
        },
        "graphs": [{
            "id": "g1",
            "balloon":{
              "drop":true,
              "adjustBorderColor":false,
              "color":"#ffffff"
            },
            "bullet": "round",
            "bulletBorderAlpha": 1,
            "bulletColor": "#FFFFFF",
            "bulletSize": 5,
            "hideBulletsCount": 50,
            "lineThickness": 2,
            "title": "red line",
            "useLineColorForBulletBorder": true,
            "valueField": "value",
            "balloonText": "<span style='font-size:18px;'>[[value]]</span>"
        }],
        "chartScrollbar": {
            "graph": "g1",
            "oppositeAxis":false,
            "offset":30,
            "scrollbarHeight": 80,
            "backgroundAlpha": 0,
            "selectedBackgroundAlpha": 0.1,
            "selectedBackgroundColor": "#888888",
            "graphFillAlpha": 0,
            "graphLineAlpha": 0.5,
            "selectedGraphFillAlpha": 0,
            "selectedGraphLineAlpha": 1,
            "autoGridCount":true,
            "color":"#AAAAAA"
        },
        "chartCursor": {
            "pan": true,
            "valueLineEnabled": true,
            "valueLineBalloonEnabled": true,
            "cursorAlpha":1,
            "cursorColor":"#258cbb",
            "limitToGraph":"g1",
            "valueLineAlpha":0.2,
            "valueZoomable":true
        },
        "valueScrollbar":{
          "oppositeAxis":false,
          "offset":50,
          "scrollbarHeight":10
        },
        "categoryField": "date",
        "categoryAxis": {
            "parseDates": true,
            "dashLength": 1,
            "minorGridEnabled": true
        },
        "export": {
            "enabled": true
        },
        "dataProvider": [{
            "date": "2012-07-27",
            "value": 13
        }, {
            "date": "2012-07-28",
            "value": 11
        }, {
            "date": "2012-07-29",
            "value": 15
        }, {
            "date": "2012-07-30",
            "value": 16
        }, {
            "date": "2012-07-31",
            "value": 18
        }, {
            "date": "2012-08-01",
            "value": 13
        }, {
            "date": "2012-08-02",
            "value": 22
        }, {
            "date": "2012-08-03",
            "value": 23
        }, {
            "date": "2012-08-04",
            "value": 20
        }, {
            "date": "2012-08-05",
            "value": 17
        }, {
            "date": "2012-08-06",
            "value": 16
        }, {
            "date": "2012-08-07",
            "value": 18
        }, {
            "date": "2012-08-08",
            "value": 21
        }, {
            "date": "2012-08-09",
            "value": 26
        }, {
            "date": "2012-08-10",
            "value": 24
        }, {
            "date": "2012-08-11",
            "value": 29
        }, {
            "date": "2012-08-12",
            "value": 32
        }, {
            "date": "2012-08-13",
            "value": 18
        }, {
            "date": "2012-08-14",
            "value": 24
        }, {
            "date": "2012-08-15",
            "value": 22
        }, {
            "date": "2012-08-16",
            "value": 18
        }, {
            "date": "2012-08-17",
            "value": 19
        }, {
            "date": "2012-08-18",
            "value": 14
        }, {
            "date": "2012-08-19",
            "value": 15
        }, {
            "date": "2012-08-20",
            "value": 12
        }, {
            "date": "2012-08-21",
            "value": 8
        }, {
            "date": "2012-08-22",
            "value": 9
        }, {
            "date": "2012-08-23",
            "value": 8
        }, {
            "date": "2012-08-24",
            "value": 7
        }, {
            "date": "2012-08-25",
            "value": 5
        }, {
            "date": "2012-08-26",
            "value": 11
        }, {
            "date": "2012-08-27",
            "value": 13
        }, {
            "date": "2012-08-28",
            "value": 18
        }, {
            "date": "2012-08-29",
            "value": 20
        }, {
            "date": "2012-08-30",
            "value": 29
        }, {
            "date": "2012-08-31",
            "value": 33
        }, {
            "date": "2012-09-01",
            "value": 42
        }, {
            "date": "2012-09-02",
            "value": 35
        }, {
            "date": "2012-09-03",
            "value": 31
        }, {
            "date": "2012-09-04",
            "value": 47
        }, {
            "date": "2012-09-05",
            "value": 52
        }, {
            "date": "2012-09-06",
            "value": 46
        }, {
            "date": "2012-09-07",
            "value": 41
        }, {
            "date": "2012-09-08",
            "value": 43
        }, {
            "date": "2012-09-09",
            "value": 40
        }, {
            "date": "2012-09-10",
            "value": 39
        }, {
            "date": "2012-09-11",
            "value": 34
        }, {
            "date": "2012-09-12",
            "value": 29
        }, {
            "date": "2012-09-13",
            "value": 34
        }, {
            "date": "2012-09-14",
            "value": 37
        }, {
            "date": "2012-09-15",
            "value": 42
        }, {
            "date": "2012-09-16",
            "value": 49
        }, {
            "date": "2012-09-17",
            "value": 46
        }, {
            "date": "2012-09-18",
            "value": 47
        }, {
            "date": "2012-09-19",
            "value": 55
        }, {
            "date": "2012-09-20",
            "value": 59
        }, {
            "date": "2012-09-21",
            "value": 58
        }, {
            "date": "2012-09-22",
            "value": 57
        }, {
            "date": "2012-09-23",
            "value": 61
        }, {
            "date": "2012-09-24",
            "value": 59
        }, {
            "date": "2012-09-25",
            "value": 67
        }, {
            "date": "2012-09-26",
            "value": 65
        }, {
            "date": "2012-09-27",
            "value": 61
        }, {
            "date": "2012-09-28",
            "value": 66
        }, {
            "date": "2012-09-29",
            "value": 69
        }, {
            "date": "2012-09-30",
            "value": 71
        }, {
            "date": "2012-10-01",
            "value": 67
        }, {
            "date": "2012-10-02",
            "value": 63
        }, {
            "date": "2012-10-03",
            "value": 46
        }, {
            "date": "2012-10-04",
            "value": 32
        }, {
            "date": "2012-10-05",
            "value": 21
        }, {
            "date": "2012-10-06",
            "value": 18
        }, {
            "date": "2012-10-07",
            "value": 21
        }, {
            "date": "2012-10-08",
            "value": 28
        }, {
            "date": "2012-10-09",
            "value": 27
        }, {
            "date": "2012-10-10",
            "value": 36
        }, {
            "date": "2012-10-11",
            "value": 33
        }, {
            "date": "2012-10-12",
            "value": 31
        }, {
            "date": "2012-10-13",
            "value": 30
        }, {
            "date": "2012-10-14",
            "value": 34
        }, {
            "date": "2012-10-15",
            "value": 38
        }, {
            "date": "2012-10-16",
            "value": 37
        }, {
            "date": "2012-10-17",
            "value": 44
        }, {
            "date": "2012-10-18",
            "value": 49
        }, {
            "date": "2012-10-19",
            "value": 53
        }, {
            "date": "2012-10-20",
            "value": 57
        }, {
            "date": "2012-10-21",
            "value": 60
        }, {
            "date": "2012-10-22",
            "value": 61
        }, {
            "date": "2012-10-23",
            "value": 69
        }, {
            "date": "2012-10-24",
            "value": 67
        }, {
            "date": "2012-10-25",
            "value": 72
        }, {
            "date": "2012-10-26",
            "value": 77
        }, {
            "date": "2012-10-27",
            "value": 75
        }, {
            "date": "2012-10-28",
            "value": 70
        }, {
            "date": "2012-10-29",
            "value": 72
        }, {
            "date": "2012-10-30",
            "value": 70
        }, {
            "date": "2012-10-31",
            "value": 72
        }, {
            "date": "2012-11-01",
            "value": 73
        }, {
            "date": "2012-11-02",
            "value": 67
        }, {
            "date": "2012-11-03",
            "value": 68
        }, {
            "date": "2012-11-04",
            "value": 65
        }, {
            "date": "2012-11-05",
            "value": 71
        }, {
            "date": "2012-11-06",
            "value": 75
        }, {
            "date": "2012-11-07",
            "value": 74
        }, {
            "date": "2012-11-08",
            "value": 71
        }, {
            "date": "2012-11-09",
            "value": 76
        }, {
            "date": "2012-11-10",
            "value": 77
        }, {
            "date": "2012-11-11",
            "value": 81
        }, {
            "date": "2012-11-12",
            "value": 83
        }, {
            "date": "2012-11-13",
            "value": 80
        }, {
            "date": "2012-11-14",
            "value": 81
        }, {
            "date": "2012-11-15",
            "value": 87
        }, {
            "date": "2012-11-16",
            "value": 82
        }, {
            "date": "2012-11-17",
            "value": 86
        }, {
            "date": "2012-11-18",
            "value": 80
        }, {
            "date": "2012-11-19",
            "value": 87
        }, {
            "date": "2012-11-20",
            "value": 83
        }, {
            "date": "2012-11-21",
            "value": 85
        }, {
            "date": "2012-11-22",
            "value": 84
        }, {
            "date": "2012-11-23",
            "value": 82
        }, {
            "date": "2012-11-24",
            "value": 73
        }, {
            "date": "2012-11-25",
            "value": 71
        }, {
            "date": "2012-11-26",
            "value": 75
        }, {
            "date": "2012-11-27",
            "value": 79
        }, {
            "date": "2012-11-28",
            "value": 70
        }, {
            "date": "2012-11-29",
            "value": 73
        }, {
            "date": "2012-11-30",
            "value": 61
        }, {
            "date": "2012-12-01",
            "value": 62
        }, {
            "date": "2012-12-02",
            "value": 66
        }, {
            "date": "2012-12-03",
            "value": 65
        }, {
            "date": "2012-12-04",
            "value": 73
        }, {
            "date": "2012-12-05",
            "value": 79
        }, {
            "date": "2012-12-06",
            "value": 78
        }, {
            "date": "2012-12-07",
            "value": 78
        }, {
            "date": "2012-12-08",
            "value": 78
        }, {
            "date": "2012-12-09",
            "value": 74
        }, {
            "date": "2012-12-10",
            "value": 73
        }, {
            "date": "2012-12-11",
            "value": 75
        }, {
            "date": "2012-12-12",
            "value": 70
        }, {
            "date": "2012-12-13",
            "value": 77
        }, {
            "date": "2012-12-14",
            "value": 67
        }, {
            "date": "2012-12-15",
            "value": 62
        }, {
            "date": "2012-12-16",
            "value": 64
        }, {
            "date": "2012-12-17",
            "value": 61
        }, {
            "date": "2012-12-18",
            "value": 59
        }, {
            "date": "2012-12-19",
            "value": 53
        }, {
            "date": "2012-12-20",
            "value": 54
        }, {
            "date": "2012-12-21",
            "value": 56
        }, {
            "date": "2012-12-22",
            "value": 59
        }, {
            "date": "2012-12-23",
            "value": 58
        }, {
            "date": "2012-12-24",
            "value": 55
        }, {
            "date": "2012-12-25",
            "value": 52
        }, {
            "date": "2012-12-26",
            "value": 54
        }, {
            "date": "2012-12-27",
            "value": 50
        }, {
            "date": "2012-12-28",
            "value": 50
        }, {
            "date": "2012-12-29",
            "value": 51
        }, {
            "date": "2012-12-30",
            "value": 52
        }, {
            "date": "2012-12-31",
            "value": 58
        }, {
            "date": "2013-01-01",
            "value": 60
        }, {
            "date": "2013-01-02",
            "value": 67
        }, {
            "date": "2013-01-03",
            "value": 64
        }, {
            "date": "2013-01-04",
            "value": 66
        }, {
            "date": "2013-01-05",
            "value": 60
        }, {
            "date": "2013-01-06",
            "value": 63
        }, {
            "date": "2013-01-07",
            "value": 61
        }, {
            "date": "2013-01-08",
            "value": 60
        }, {
            "date": "2013-01-09",
            "value": 65
        }, {
            "date": "2013-01-10",
            "value": 75
        }, {
            "date": "2013-01-11",
            "value": 77
        }, {
            "date": "2013-01-12",
            "value": 78
        }, {
            "date": "2013-01-13",
            "value": 70
        }, {
            "date": "2013-01-14",
            "value": 70
        }, {
            "date": "2013-01-15",
            "value": 73
        }, {
            "date": "2013-01-16",
            "value": 71
        }, {
            "date": "2013-01-17",
            "value": 74
        }, {
            "date": "2013-01-18",
            "value": 78
        }, {
            "date": "2013-01-19",
            "value": 85
        }, {
            "date": "2013-01-20",
            "value": 82
        }, {
            "date": "2013-01-21",
            "value": 83
        }, {
            "date": "2013-01-22",
            "value": 88
        }, {
            "date": "2013-01-23",
            "value": 85
        }, {
            "date": "2013-01-24",
            "value": 85
        }, {
            "date": "2013-01-25",
            "value": 80
        }, {
            "date": "2013-01-26",
            "value": 87
        }, {
            "date": "2013-01-27",
            "value": 84
        }, {
            "date": "2013-01-28",
            "value": 83
        }, {
            "date": "2013-01-29",
            "value": 84
        }, {
            "date": "2013-01-30",
            "value": 81
        }]
    }, 1);

    this.chart.addListener('rendered', this.zoomChart);
    this.zoomChart();
  }

  zoomChart() {
    this.chart.zoomToIndexes(this.chart.dataProvider.length - 40, this.chart.dataProvider.length - 1);
  }
}
