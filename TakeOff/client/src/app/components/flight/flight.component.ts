import { Component, OnInit } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { FlightService } from 'src/app/services/flight/flight.service';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { FlightDialogComponent } from '../flight-dialog/flight-dialog.component';
import { FlightReservationComponent } from '../flight-reservation/flight-reservation.component';
import { AppComponent } from 'src/app/app.component';

declare let SeatchartJS: any;

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
    disabledCols: []
  };
  types = [
    { type: 'first_class', color: 'orange', price: 10, selected: [] },
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

          this.flightExists = true;
          this.loadingFlight = false;
          this.userRole = this.authService.getAuthority();
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

      this.flightService.getFlightTickets(id).subscribe(
        (data: []) => {
          this.tickets = data;
          this.loadingTickets = false;
          for (let i = 0; i < data.length; i++) {
            const ticket: any = data[i];
            if (ticket.reserved) {
              this.map.reserved.push(ticket.number);
            }
          }
          this.createSeatChart();
        },
        error => {

        }
      );
    } else {

    }
  }

  addRow() {
    if (this.sc) {
      if (!this.sc.addRow()) {
        alert('ne moze vise');
      }
    }
  }

  addColumn() {
    if (this.sc) {
      if (!this.sc.addColumn()) {
        alert('dosta ti je 30');
      }
    }
  }

  getCart() {
    if (this.sc) {
      console.log(this.sc.getShoppingCart());
      console.log(this.sc.getTotal());
    }
  }

  createSeatChart() {
    this.sc = new SeatchartJS(this.map, this.types);
    this.sc.setAssetsSrc('assets/seatchart');

    // (1) Create functions
    this.sc.createMap('map-container');
    this.sc.createLegend('legend-container');
    this.sc.createShoppingCart('shoppingCart-container');
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
          'total': total
        },
        disableClose: true,
        autoFocus: true,
        height: '90%',
        width: '60%',
      });

    dialogRef.afterClosed().subscribe(
      (friends) => {
        const cart = this.sc.getShoppingCart();
        const reservations = [];
        let friendIndex = 0;
        for (const key of Object.keys(cart)) {
          for (const n of cart[key]) {
            const ticket = this.tickets.find(t => {
              return t.number === n;
            });
            ticket['reserved'] = true; // optional
            ticket['type'] = key; // seat class
            const user = friends[friendIndex];
            reservations.push({ 'user': user, 'ticket': ticket });
            friendIndex++;
          }
        }
        console.log(reservations);
        this.reservationService.createReservations(reservations).subscribe(
          () => {
            this.appComponent.showSnackBar('Reservation successfull!');
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

}
