import { Component, OnInit } from '@angular/core';
import { RegistrationService } from 'src/app/services/registration/registration.service';
import { ActivatedRoute, Router } from '@angular/router';

enum VerificationStatus {
  OK = 200,
  NOT_FOUND = 404,
  BAD_REQUEST = 400
}

@Component({
  selector: 'app-verify-user',
  templateUrl: './verify-user.component.html',
  styleUrls: ['./verify-user.component.css']
})
export class VerifyUserComponent implements OnInit {

  constructor(private registrationService: RegistrationService,
              private activatedRoute: ActivatedRoute,
              private router: Router) { }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      const token = params['token'];
      this.registrationService.verifyUser(token).subscribe(
        () => {
          alert('Verified successfully! Please log in to continue browsing our site.');
          this.router.navigate(['/']);
        },
        (error) => {
            let message = '';
            if (error.status === 400) {
              message = 'Error! User is already verified!';
            } else if (error.status === 404) {
              message = 'Error! User is not found!';
            }

            alert(message);
            this.router.navigate(['/']);
        }
      );
    });
  }
}
