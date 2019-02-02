import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  userRole: string = null;

  constructor(private authService: AuthenticationService) { }

  ngOnInit() {
    this.authService.onSubject.subscribe(
      () => {
        this.userRole = this.authService.getAuthority();
      }
    );
    this.userRole = this.authService.getAuthority();
  }

}
