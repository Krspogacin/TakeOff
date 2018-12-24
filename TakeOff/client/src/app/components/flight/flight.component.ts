import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FlightService } from 'src/app/services/flight/flight.service';

@Component({
  selector: 'app-flight',
  templateUrl: './flight.component.html',
  styleUrls: ['./flight.component.css']
})
export class FlightComponent implements OnInit {

  flight = {};
  destinations = [];

  constructor(private flightService: FlightService, private route: ActivatedRoute) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.flightService.getFlightById(id).subscribe(
        (data) => {
          this.flight = data;
        },
        error => {
          // handle not found error
        });

      this.flightService.getFlightDestinations(id).subscribe(
        (data: []) => {
          this.destinations = data;
        },
        error => {

        }
      );
    } else {

    }
  }

}
