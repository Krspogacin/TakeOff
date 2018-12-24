import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { FlightService } from 'src/app/services/flight/flight.service';

@Component({
  selector: 'app-air-company',
  templateUrl: './air-company.component.html',
  styleUrls: ['./air-company.component.css']
})
export class AirCompanyComponent implements OnInit {

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
    private flightService: FlightService) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.airCompanyService.getCompanyById(id).subscribe(
        (data) => {
          this.company = data;
        },
        error => {
          // handle not found error
        });

      this.airCompanyService.getCompanyDestinations(id).subscribe(
        (data: []) => {
          this.destinations = data;
        }, error => {

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
        },
        error => {

        }
      );
    } else {
      // not found
    }
  }

  updateCompany() {
    this.airCompanyService.updateCompany(this.company).subscribe(
      (data) => {
        alert('Updated!');
      },
      error => {
        alert('Error!');
      }
    );
  }

  addFlight() {
    this.form.company = this.company;
    const destinations = this.form.destinations;
    console.log(destinations);
    delete this.form.destinations;
    console.log(this.form);

    this.flightService.addFlight(this.form).subscribe(
      (data) => {
        alert('Flight added!');
        this.flights.push(data);
      },
      error => {
        alert('Error!');
      }
    );
  }

}
