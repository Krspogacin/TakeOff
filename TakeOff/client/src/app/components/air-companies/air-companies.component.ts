import { Component, OnInit } from '@angular/core';
import { MatDialog, PageEvent } from '@angular/material';
import { AppComponent } from 'src/app/app.component';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { AddEntityDialogComponent } from '../add-entity-dialog/add-entity-dialog.component';

declare let require: any;

@Component({
  selector: 'app-air-companies',
  templateUrl: './air-companies.component.html',
  styleUrls: ['./air-companies.component.css']
})
export class AirCompaniesComponent implements OnInit {

  loading = true;
  companies = [];
  pageSize = 10;
  pageIndex = 0;
  message: string;
  userRole: string;
  sortOptions = [
    { value: 'nameAsc', viewValue: 'Name', icon: 'arrow_upward', sortBy: 'name', asc: true },
    { value: 'nameDesc', viewValue: 'Name', icon: 'arrow_downward', sortBy: 'name', asc: false },
    { value: 'ratingAsc', viewValue: 'Rating', icon: 'arrow_upward', sortBy: 'rating', asc: true },
    { value: 'ratingDesc', viewValue: 'Rating', icon: 'arrow_downward', sortBy: 'rating', asc: false }
  ];

  selected = this.sortOptions[0].value;
  selectedOption = this.sortOptions[0];
  search = false;
  flights = [];
  minDate = new Date();
  searchObject = {
    'companyId': null,
    'departure': null,
    'arrival': null,
    'date': null
  };

  constructor(private authService: AuthenticationService, private airCompanyService: AirCompanyService,
    private dialog: MatDialog, private appComponent: AppComponent) { }

  ngOnInit() {
    this.airCompanyService.getCompanies().subscribe(
      (data: []) => {
        this.companies = data;
        this.userRole = this.authService.getAuthority();
        this.airCompanyService.getCompaniesRatings().subscribe(
          (ratings) => {
            // tslint:disable-next-line:forin
            for (const i in this.companies) {
              this.companies[i].rating = ratings[i];
              // this.companies[i].ratingToShow = ratings[i];
            }
            this.companies = this.appComponent.sortArray(data, 'name', true);
            this.loading = false;
          }
        );
      }
    );

    this.authService.onSubject.subscribe(
      (data) => {
        this.userRole = this.authService.getAuthority();
      }
    );
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddEntityDialogComponent,
      {
        data: 0,
        disableClose: true,
        autoFocus: true,
        width: '60%',
        height: '90%'
      });
    dialogRef.afterClosed().subscribe(
      (result) => {
        if (result) {
          this.message = 'Added successfully!';
          const airCompany = result;
          airCompany.rating = 0;
          this.companies.push(airCompany);
        }
      },
      () => {
        this.message = 'Error! Air company could not be added!';
      },
      () => {
        this.appComponent.showSnackBar(this.message);
      }
    );
  }

  switchPage(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
  }

  sort(value: any) {
    for (const i in this.sortOptions) {
      if (this.sortOptions[i].value === value) {
        this.selectedOption = this.sortOptions[i];
      }
    }
    if (!this.search) {
      this.companies = this.appComponent.sortArray(this.companies, this.selectedOption.sortBy, this.selectedOption.asc);
    } else {
      this.flights = this.appComponent.sortArray(this.flights, this.selectedOption.sortBy, this.selectedOption.asc);
    }
  }

  searchFlights(departure, arrival, date) {
    this.searchObject.departure = departure;
    this.searchObject.arrival = arrival;
    this.searchObject.date = date ? new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate())) : null;

    this.airCompanyService.searchFlights(this.searchObject).subscribe(
      (flights: []) => {
        this.flights = flights;
        this.search = true;
      }
    );
  }

}
