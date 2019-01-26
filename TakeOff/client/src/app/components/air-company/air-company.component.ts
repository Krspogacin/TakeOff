import { Component, OnInit } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { FlightService } from 'src/app/services/flight/flight.service';
import { AirCompanyDialogComponent } from '../air-company-dialog/air-company-dialog.component';
import { FlightDialogComponent } from '../flight-dialog/flight-dialog.component';

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
  userRoles: any;
  message: string;
  company = {};
  destinations = [];
  flights = [];
  form = {
    takeOffDate: null,
    landingDate: null,
    distance: null,
    numberOfTransfers: null,
    destinations: null,
    ticketPrice: null,
    company: null
  };

  constructor(private airCompanyService: AirCompanyService, private route: ActivatedRoute,
    private flightService: FlightService, private authService: AuthenticationService, private dialog: MatDialog,
    private snackBar: MatSnackBar,
  ) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.airCompanyService.getCompanyById(id).subscribe(
        (data) => {
          this.company = data;
          this.userRoles = this.authService.getAuthorities();
          this.companyExists = true;
          this.loadingCompany = false;
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
            this.flightService.getFlightDestinations(flight.id).subscribe(
              (dest) => {
                flight.destinations = dest;
              }
            );
          });
          this.loadingFlights = false;
        },
        error => {
          this.loadingFlights = false;
        }
      );

    } else {
      // not found
    }
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
              this.message = 'Updated successfully!';
              this.airCompanyService.setCompanyDestinations(updated.company.id, updated.destinations).subscribe(
                (destinations: []) => {
                  this.company = data;
                  this.destinations = destinations;
                }
              );
            },
            error => {
              this.message = 'Error updating air company!';
            },
            () => {
              this.showSnackBar();
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
              this.showSnackBar();
            }
          );

        }
      }
    );
  }

  showSnackBar() {
    if (!this.message) {
      return;
    }
    const snackBarRef = this.snackBar.open(this.message, 'Dismiss', { duration: 3000 });
    snackBarRef.onAction().subscribe(
      () => {
        snackBarRef.dismiss();
      }
    );
  }

}
