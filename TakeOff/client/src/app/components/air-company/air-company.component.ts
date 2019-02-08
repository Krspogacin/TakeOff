import { Component, OnInit } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { FlightService } from 'src/app/services/flight/flight.service';
import { AirCompanyDialogComponent } from '../air-company-dialog/air-company-dialog.component';
import { FlightDialogComponent } from '../flight-dialog/flight-dialog.component';
import { SafeResourceUrl, DomSanitizer } from '@angular/platform-browser';
import { AppComponent } from 'src/app/app.component';
import { UserService } from 'src/app/services/user/user.service';
import { ReservationService } from 'src/app/services/reservation/reservation.service';

@Component({
  selector: 'app-air-company',
  templateUrl: './air-company.component.html',
  styleUrls: ['./air-company.component.css']
})
export class AirCompanyComponent implements OnInit {

  companyExists = false;
  loadingCompany = true;
  loadingFlights = true;
  loadingDestinations = true;
  userRole: any;
  message: string;
  company: any;
  destinations = [];
  flights = [];
  mapUrl: SafeResourceUrl;
  rating: number;
  onDiscountTickets = [];
  user = {};

  constructor(private airCompanyService: AirCompanyService, private route: ActivatedRoute,
    private flightService: FlightService, private authService: AuthenticationService, private dialog: MatDialog,
    private sanitizer: DomSanitizer, private appComponent: AppComponent, private userService: UserService,
    private reservationService: ReservationService) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.airCompanyService.getCompanyById(id).subscribe(
        (data) => {
          this.company = data;
          this.userRole = this.authService.getAuthority();
          this.companyExists = true;
          this.airCompanyService.getCompanyRating(id).subscribe(
            (rating: number) => {
              this.rating = rating;
              this.loadingCompany = false;
            }
          );
          this.mapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
            'https://maps.google.com/maps?q=' +
            this.company.location.latitude + ', ' +
            this.company.location.longitude +
            '&t=&z=11&ie=UTF8&iwloc=&output=embed');
        },
        error => {
          this.loadingCompany = false;
        });

      this.airCompanyService.getCompanyDestinations(id).subscribe(
        (data: []) => {
          this.destinations = data;
          this.loadingDestinations = false;
        }, error => {
          this.loadingDestinations = false;
        }
      );

      this.airCompanyService.getCompanyFlights(id).subscribe(
        (data: []) => {
          this.flights = data;
          this.flights.forEach(flight => {
            // get flight destinations
            this.flightService.getFlightDestinations(flight.id).subscribe(
              (dest) => {
                flight.destinations = dest;
              }
            );

            // get tickets on discount for fast reservation
            this.flightService.getFlightTickets(flight.id).subscribe(
              (tickets: []) => {
                tickets.forEach((element: any) => {
                  if (element.onDiscount && !element.reserved) {
                    this.onDiscountTickets.push(element);
                  }
                });
              });
          });
          this.loadingFlights = false;
        },
        error => {
          this.loadingFlights = false;
        }
      );

      const username = this.authService.getUsername();
      this.userService.getUser(username).subscribe(
        (data) => {
          this.user = data;
        });
    } else {
      // not found
    }

    this.authService.onSubject.subscribe(
      () => {
        this.userRole = this.authService.getAuthority();
      }
    );
  }

  openUpdateDialog() {
    const dialogRef = this.dialog.open(AirCompanyDialogComponent,
      {
        data: {
          'company': this.company,
          'destinations': this.destinations
        },
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });

    dialogRef.afterClosed().subscribe(
      (updated) => {
        if (updated) {
          this.airCompanyService.updateCompany(updated.company).subscribe(
            (data) => {
              this.airCompanyService.setCompanyDestinations(updated.company.id, updated.destinations).subscribe(
                (destinations: []) => {
                  this.company = data;
                  this.destinations = destinations;
                  this.message = 'Updated successfully!';
                }
              );
            },
            error => {
              this.message = 'Error updating air company!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openAddFlightDialog() {
    const dialogRef = this.dialog.open(FlightDialogComponent,
      {
        data: null,
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          data.flight.company = this.company;
          this.flightService.addFlight(data.flight).subscribe(
            (flight: any) => {
              this.flights.push(flight);
              this.message = 'Flight successfully added!';
              this.flightService.setFlightDestinations(flight.id, data.destinations).subscribe(
                (destinations: []) => {
                  flight.destinations = destinations;
                },
                error => {
                }
              );
            },
            error => {
              this.message = 'Error creating flight!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );

        }
      }
    );
  }

  createFastReservation(ticket) {
    const reservations = [{ 'user': this.user, 'ticket': ticket }];

    this.reservationService.createReservations(reservations).subscribe(
      () => {
        this.appComponent.showSnackBar('Reservation successful!');
        const index = this.onDiscountTickets.indexOf(ticket);
        if (index > -1) {
          this.onDiscountTickets.splice(index, 1);
        }
      },
      () => {
        this.appComponent.showSnackBar('Reservation failed. Please try again.');
      }
    );

  }

}
