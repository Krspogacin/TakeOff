import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FlightService } from 'src/app/services/flight/flight.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { MatDialog, MatSnackBar } from '@angular/material';
import { FlightDialogComponent } from '../flight-dialog/flight-dialog.component';

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
  userRoles: any;
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
    private snackBar: MatSnackBar) { }

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
          this.userRoles = this.authService.getAuthorities();
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
