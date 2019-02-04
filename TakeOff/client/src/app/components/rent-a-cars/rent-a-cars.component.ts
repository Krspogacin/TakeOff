import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { PageEvent, MatDialog, MatSnackBar, MatSelect } from '@angular/material';
import { RentACarDialogComponent } from '../rent-a-car-dialog/rent-a-car-dialog.component';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';
import { AppComponent } from 'src/app/app.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { VehicleReservationDialogComponent } from '../vehicle-reservation-dialog/vehicle-reservation-dialog.component';

@Component({
  selector: 'app-rent-a-cars',
  templateUrl: './rent-a-cars.component.html',
  styleUrls: ['./rent-a-cars.component.css']
})
export class RentACarsComponent implements OnInit {

  rentACars = [];
  pageSize = 10;
  pageIndex = 0;
  message: string;
  userRole: string;
  loading = true;
  sortOptions = [{value: 'nameAsc', viewValue: 'Name', icon: 'arrow_upward', sortBy: 'name', asc: true},
                 {value: 'nameDesc', viewValue: 'Name', icon: 'arrow_downward', sortBy: 'name', asc: false},
                 {value: 'ratingAsc', viewValue: 'Rating', icon: 'arrow_upward', sortBy: 'rating', asc: true},
                 {value: 'ratingDesc', viewValue: 'Rating', icon: 'arrow_downward', sortBy: 'rating', asc: false}];
  selected = this.sortOptions[0].value;
  selectedOption = this.sortOptions[0];
  nameFilterParam = '';
  countryFilterParam = '';
  cityFilterParam = '';

  constructor(private authService: AuthenticationService,
              private rentACarService: RentACarService,
              public dialog: MatDialog,
              public appComponent: AppComponent) { }

  ngOnInit() {
    this.rentACarService.getRentACars().subscribe(
      (data: []) => {
        this.rentACars = data;
        this.userRole = this.authService.getAuthority();
        this.rentACarService.getRentACarRatings().subscribe(
          (ratings) => {
            // tslint:disable-next-line:forin
            for (const i in this.rentACars) {
                this.rentACars[i].rating = ratings[i];
                // this.rentACars[i].ratingToShow = ratings[i];
            }
            this.rentACars = this.appComponent.sortArray(data, 'name', true);
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

  // showRating(index: number) {
  //   this.rentACars[index].ratingToShow = this.rentACars[index].rating;
  // }

  // hoverRating(index: number, rating: number) {
  //   this.rentACars[index].ratingToShow = rating;
  // }

  openAddRentACarDialog() {
    const dialogRef = this.dialog.open(RentACarDialogComponent,
    {
      data: null,
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          this.rentACarService.addRentACar(data).subscribe(
            (rentACar: any) => {
              this.message = 'Added successfully!';
              const newRentACar = rentACar;
              newRentACar.rating = 0;
              this.rentACars.push(newRentACar);
              this.rentACars = this.appComponent.sortArray(this.rentACars, this.selectedOption.sortBy, this.selectedOption.asc);
            },
            () => {
              this.message = 'Error! Rent A Car could not be added!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
    });
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
    this.rentACars = this.appComponent.sortArray(this.rentACars, this.selectedOption.sortBy, this.selectedOption.asc);
  }

  search(searchName: string, countryParam: string, cityParam: string) {
    this.nameFilterParam = searchName;
    this.countryFilterParam = countryParam;
    this.cityFilterParam = cityParam;
  }

  openReservationDialog(rentACar) {
    const dialogRef = this.dialog.open(VehicleReservationDialogComponent,
    {
      data: rentACar,
      disableClose: true,
      autoFocus: true,
      width: '60%',
      height: '90%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
      }
    );
  }
}
