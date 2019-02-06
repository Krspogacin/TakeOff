import { Component, OnInit } from '@angular/core';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {

  reservations = [];
  loading = true;
  exists = false;

  constructor(private authService: AuthenticationService,
    private reservationService: ReservationService) { }

  ngOnInit() {
    const username = this.authService.getUsername();
    if (username) {
      this.reservationService.getReservations(username).subscribe(
        (data: []) => {
          console.log(data);
          this.reservations = data;
          this.loading = false;
          this.exists = data.length > 0;
        },
        () => {
          this.loading = false;
        }
      );
    }
  }

}
