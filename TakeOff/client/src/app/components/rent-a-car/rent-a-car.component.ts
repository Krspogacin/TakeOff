import { Component, OnInit } from '@angular/core';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog, MatSnackBar, MatSnackBarRef } from '@angular/material';
import { RentACarDialogComponent } from '../rent-a-car-dialog/rent-a-car-dialog.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { VehicleService } from 'src/app/services/vehicle/vehicle.service';
import { VehicleDialogComponent } from '../vehicle-dialog/vehicle-dialog.component';

@Component({
  selector: 'app-rent-a-car',
  templateUrl: './rent-a-car.component.html',
  styleUrls: ['./rent-a-car.component.css']
})
export class RentACarComponent implements OnInit {

  loadingRentACar = true;
  loadingVehicles = true;
  rentACarExists = false;
  rentACar: any = null;
  vehicles: any = null;
  message: string;
  userRoles: Array<any>;

  constructor(private rentACarService: RentACarService,
              private authService: AuthenticationService,
              private route: ActivatedRoute,
              public dialog: MatDialog,
              public snackBar: MatSnackBar,
              private vehicleService: VehicleService) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.rentACarService.getRentACarById(id).subscribe(
        (data) => {
          this.rentACar = data;
          this.userRoles = new Array();
          this.userRoles.push.apply(this.userRoles, this.authService.getAuthorities());
          this.rentACarExists = true;
          this.loadingRentACar = false;
        },
        (error) => {
          this.loadingRentACar = false;
        }
      );

      this.rentACarService.getVehicles(id).subscribe(
        (vehicles) => {
          this.vehicles = vehicles;
          this.loadingVehicles = false;
        },
        (error) => {
          this.loadingVehicles = false;
        }
      );

    }
  }

  openUpdateDialog() {
    const dialogRef = this.dialog.open(RentACarDialogComponent,
    {
      data: this.rentACar,
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          this.rentACarService.updateRentACar(data).subscribe(
            (rentacar) => {
              this.message = 'Updated successfully!';
              this.rentACar = rentacar;
            },
            () => {
              this.message = 'Error! Rent A Car is not found!';
            },
            () => {
              const snackBarRef = this.snackBar.open(this.message, 'Dissmiss', { duration: 3000 });
              snackBarRef.onAction().subscribe(
                () => {
                  snackBarRef.dismiss();
                }
              );
            }
          );
        }
      }
    );
  }

  openAddVehicleDialog() {
    const dialogRef = this.dialog.open(VehicleDialogComponent,
    {
      data: null,
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          let newVehicle = data;
          newVehicle.rentACar = this.rentACar;
          this.vehicleService.addVehicle(newVehicle).subscribe(
            (vehicle) => {
              newVehicle = vehicle;
              this.vehicles.push(newVehicle);
              this.message = 'Added vehicle successfully!';
            },
            () => {
              this.message = 'Error! Vehicle could not be added!';
            },
            () => {
              this.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openUpdateVehicleDialog(vehicle: any) {
    const dialogRef = this.dialog.open(VehicleDialogComponent,
      {
        data: vehicle,
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });
      dialogRef.afterClosed().subscribe(
        (data) => {
          if (data) {
            const updatedVehicle = data;
            this.vehicleService.updateVehicle(updatedVehicle).subscribe(
              () => {
                for (const i in this.vehicles) {
                  if (this.vehicles[i].id === updatedVehicle.id) {
                    this.vehicles.splice(i, 1, updatedVehicle);
                  }
                }
                this.message = 'Updated vehicle successfully!';
              },
              () => {
                this.message = 'Error! Vehicle could not be updated!';
              },
              () => {
                this.showSnackBar(this.message);
              }
            );
          }
        }
      );
  }

  deleteVehicle(id: number) {
    this.vehicleService.deleteVehicle(id).subscribe(
      () => {
        for (const i in this.vehicles) {
          if (this.vehicles[i].id === id) {
            this.vehicles.splice(i, 1);
            break;
          }
        }
        this.message = 'Deleted vehicle successfully!';
      },
      () => {
        this.message = 'Error! Vehicle could not be deleted!';
      },
      () => {
        this.showSnackBar(this.message);
      }
    );
  }

  showSnackBar(message: string) {
    if (!message) {
      return;
    }
    const snackBarRef = this.snackBar.open(message, 'Dissmiss', { duration: 3000 });
    snackBarRef.onAction().subscribe(
      () => {
        snackBarRef.dismiss();
      }
    );
  }
}
