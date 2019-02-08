import { Component, OnInit } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { FlightService } from 'src/app/services/flight/flight.service';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { FlightDialogComponent } from '../flight-dialog/flight-dialog.component';
import { FlightReservationComponent } from '../flight-reservation/flight-reservation.component';
import { AppComponent } from 'src/app/app.component';

declare const SeatchartJS: any;

@Component({
  selector: 'app-flight',
  templateUrl: './flight.component.html',
  styleUrls: ['./flight.component.css']
})
export class FlightComponent implements OnInit {

  flightExists = false;
  loadingFlight = true;
  loadingDestinations = true;
  loadingTickets = true;
  userRole: any;
  message: string;
  flight: any;
  destinations = [];
  tickets = [];
  map = {
    rows: 0,
    cols: 0,
    // e.g. Reserved Seat [Row: 1, Col: 2] = 7 * 1 + 2 = 9
    reserved: [],
    disabled: [0, 8],
    disabledRows: [],
    disabledCols: [],
    onDiscount: []
  };
  types = [
    { type: 'first-class', color: 'orange', price: 10, selected: [] },
    { type: 'business', color: '#af0000', price: 7.5, selected: [] },
    { type: 'regular', color: 'red', price: 7, selected: [] }
  ];
  sc;

  constructor(private flightService: FlightService, private route: ActivatedRoute,
    private authService: AuthenticationService, private dialog: MatDialog,
    private reservationService: ReservationService, private appComponent: AppComponent) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.flightService.getFlightById(id).subscribe(
        (data) => {
          this.flight = data;
          this.map.rows = this.flight.diagram.rows;
          this.map.cols = this.flight.diagram.cols;
          this.map.disabledRows = this.flight.diagram.disabledRows;
          this.map.disabledCols = this.flight.diagram.disabledCols;

          this.types[0].price = this.flight.ticketPrice * 2;
          this.types[1].price = this.flight.ticketPrice * 1.5;
          this.types[2].price = this.flight.ticketPrice;

          this.flightExists = true;
          this.loadingFlight = false;
          this.userRole = this.authService.getAuthority();

          this.flight.distance = this.appComponent.calculateDistance(this.flight);

          this.flightService.getFlightTickets(id).subscribe(
            (tickets: []) => {
              this.tickets = tickets;
              this.loadingTickets = false;
              for (let i = 0; i < tickets.length; i++) {
                const ticket: any = tickets[i];
                if (ticket.reserved) {
                  this.map.reserved.push(ticket.number);
                }
                if (ticket.onDiscount) {
                  if (this.userRole === 'ROLE_USER') {
                    this.map.reserved.push(ticket.number);
                  } else if (this.userRole === 'ROLE_AIRCOMPANY_ADMIN') {
                    this.map.onDiscount.push(ticket.number);
                  }
                }
              }
              this.createSeatChart();
            },
            error => {

            }
          );

          // this.createFlyingMap();
        },
        error => {
          this.loadingFlight = false;
        });

      this.flightService.getFlightDestinations(id).subscribe(
        (data: []) => {
          this.destinations = data;
          this.loadingDestinations = false;
        },
        error => {
          this.loadingDestinations = false;
        }
      );
    } else {

    }
  }

  addRow() {
    if (this.sc) {
      if (!this.sc.addRow()) {
        alert('You can have maximum 26 rows!');
      }
    }
  }

  createSeatChart() {
    this.sc = new SeatchartJS(this.map, this.types, this.userRole);
    this.sc.setAssetsSrc('assets/seatchart');

    // (1) Create functions
    this.sc.createMap('map-container');
    this.sc.createLegend('legend-container');
    if (this.userRole === 'ROLE_USER') {
      this.sc.createShoppingCart('shoppingCart-container');
    }
  }

  openUpdateDialog() {
    const dialogRef = this.dialog.open(FlightDialogComponent,
      {
        data: {
          'flight': this.flight,
          'destinations': this.destinations
        },
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });

    dialogRef.afterClosed().subscribe(
      (updated) => {
        if (updated) {
          this.flightService.updateFlight(updated.flight).subscribe(
            (flight: any) => {
              this.message = 'Updated successfully!';
              this.flightService.setFlightDestinations(flight.id, updated.destinations).subscribe(
                (destinations: []) => {
                  this.flight = flight;
                  this.destinations = destinations;
                }
              );
            },
            error => {
              this.message = 'Error updating flight!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  bookFlight() {

    let seats = 0;
    if (!this.sc) {
      return;

    } else {
      const cart = this.sc.getShoppingCart();
      // check if cart is empty

      for (let i = 0; i < this.types.length; i++) {
        seats += cart[this.types[i]['type']].length;
      }

      if (seats === 0) {
        this.message = 'Please first select one or more seats from the diagram.';
        this.appComponent.showSnackBar(this.message);
        return;
      }
    }

    const total = this.sc.getTotal();
    const dialogRef = this.dialog.open(FlightReservationComponent,
      {
        data: {
          'seats': seats,
          'total': total,
          'landingDate': this.flight.landingDate,
          'landingLocation': this.flight.landingLocation
        },
        disableClose: true,
        autoFocus: true,
        height: '90%',
        width: '60%',
      });

    dialogRef.afterClosed().subscribe(
      (data) => {
        const cart = this.sc.getShoppingCart();
        const reservations = [];
        let friendIndex = 0;
        const reservationDTO = { 'roomReservation': data.roomReservation, 'vehicleReservation': data.vehicleReservation };
        for (const key of Object.keys(cart)) {
          for (const n of cart[key]) {
            const ticket = this.tickets.find(t => {
              return t.number === n;
            });
            ticket['reserved'] = true; // optional
            ticket['type'] = key; // seat class
            const user = data.friends[friendIndex];
            reservations.push({ 'user': user, 'ticket': ticket, 'reservationDTO': reservationDTO });
            friendIndex++;
          }
        }

        this.reservationService.createReservations(reservations).subscribe(
          () => {
            this.appComponent.showSnackBar('Reservation successful!');
          },
          () => {
            this.appComponent.showSnackBar('Reservation failed. Please try again.');
          },
          () => {
            setTimeout(function () { location.reload(); }, 3000);
          }
        );
      }
    );
  }

  saveDiagram() {
    if (this.sc) {
      const map = this.sc.getSeatMap();

      this.flight.diagram.rows = map.rows;
      this.flight.diagram.cols = map.cols;
      this.flight.diagram.disabledRows = map.disabledRows;
      this.flight.diagram.disabledCols = map.disabledCols;

      for (const ticket of this.tickets) {
        for (const ticketOnD of map.onDiscount) {
          if (ticket.number === ticketOnD) {
            ticket.onDiscount = true;
            break;
          } else {
            ticket.onDiscount = false;
          }
        }
      }

      this.flightService.updateFlightDiagram(this.flight.id, this.flight.diagram).subscribe(
        () => {
          this.flightService.updateFlightTickets(this.tickets).subscribe(
            () => {
              this.appComponent.showSnackBar('Updated successfully!');
            },
            () => {
              this.appComponent.showSnackBar('Error while updating seats!');
            }
          );
        },
        () => {
          this.appComponent.showSnackBar('Error while updating diagram!');
        },
        () => {
          this.appComponent.showSnackBar(this.message);
          setTimeout(function () { location.reload(); }, 3000);
        }
      );
    }
  }

}
