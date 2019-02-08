import { Component, OnInit, ViewChild, TemplateRef, AfterViewInit, OnDestroy } from '@angular/core';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog, PageEvent, MatPaginator, MatTableDataSource, MatTable, MatTableModule} from '@angular/material';
import { RentACarDialogComponent } from '../rent-a-car-dialog/rent-a-car-dialog.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { VehicleService } from 'src/app/services/vehicle/vehicle.service';
import { VehicleDialogComponent } from '../vehicle-dialog/vehicle-dialog.component';
import { AppComponent } from 'src/app/app.component';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Subject } from 'rxjs';
import { RentACarMainServiceDialogComponent } from '../rent-a-car-main-service-dialog/rent-a-car-main-service-dialog.component';
import { OfficeDialogComponent } from '../office-dialog/office-dialog.component';
import { AmChartsService, AmChart } from '@amcharts/amcharts3-angular';

@Component({
  selector: 'app-rent-a-car',
  templateUrl: './rent-a-car.component.html',
  styleUrls: ['./rent-a-car.component.css']
})
export class RentACarComponent implements OnInit, AfterViewInit, OnDestroy {

  loadingRentACar = true;
  loadingVehicles = true;
  loadingMainServices = true;
  rentACarExists = false;
  rentACar: any = null;
  vehicles: any = null;
  message: string;
  userRole: string = null;
  pageSize = 10;
  pageIndex = 0;
  pageOptions = [5, 10, 25];
  rating: number;
  mapUrl: SafeResourceUrl;
  mainServicesDataSource = [];
  columns = [];
  displayedColumns = [];
  mainServicesLoaderSubject = new Subject<{ key: string, value: any }>();
  mainServices: [[]];
  dataSource = new MatTableDataSource<Element[]>();
  @ViewChild(MatPaginator) paginator: MatPaginator;
  offices: any = null;
  officeMapUrl: SafeResourceUrl;
  officeLocation: string;
  private chart: AmChart;
  id: number;

  constructor(private rentACarService: RentACarService,
              private authService: AuthenticationService,
              private route: ActivatedRoute,
              public dialog: MatDialog,
              public appComponent: AppComponent,
              private vehicleService: VehicleService,
              private sanitizer: DomSanitizer,
              private amCharts: AmChartsService) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    this.id = id;
    if (!isNaN(id)) {
      this.rentACarService.getRentACarById(id).subscribe(
        (data) => {
          this.rentACar = data;
          this.userRole = this.authService.getAuthority();
          this.rentACarExists = true;
          this.mapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
                    'https://maps.google.com/maps?q=' +
                    this.rentACar.location.latitude + ', ' +
                    this.rentACar.location.longitude +
                    '&t=&z=11&ie=UTF8&iwloc=&output=embed');
          this.rentACarService.getRentACarRating(id).subscribe(
            (rating: number) => {
              this.rating = rating;
              this.loadingRentACar = false;
            }
          );
        },
        () => {
          this.loadingRentACar = false;
        }
      );

      this.rentACarService.getVehicles(id).subscribe(
        (vehicles) => {
          this.vehicles = vehicles;
          this.rentACarService.getVehiclesRatings(id).subscribe(
            (ratings) => {
              // tslint:disable-next-line:forin
              for (const i in this.vehicles) {
                this.vehicles[i].rating = ratings[i];
              }
              this.loadingVehicles = false;
              this.mainServicesLoaderSubject.next({key: 'loadedVehicles', value: true});
            }
          );
        },
        () => {
          this.loadingVehicles = false;
        }
      );

      this.rentACarService.getOffices(id).subscribe(
        (offices) => {
          this.offices = offices;
          if (this.offices.length > 0) {
            this.officeLocation = this.offices[0].location.latitude + ', ' + this.offices[0].location.longitude;
            this.updateOfficeLocationMap();
            }
        }
      );

      this.authService.onSubject.subscribe(
        () => {
          this.userRole = this.authService.getAuthority();
        }
      );

      this.rentACarService.getMainServicesPrices(id).subscribe(
        (data: [[]]) => {
          this.mainServices = data;
          this.loadingMainServices = false;
          this.mainServicesLoaderSubject.next({key: 'loadedMainServices', value: true});
        },
        () => {
          this.loadingMainServices = false;
        }
      );

      this.mainServicesLoaderSubject.subscribe(
        () => {
          if (!this.loadingVehicles && !this.loadingMainServices &&
              this.mainServices && this.vehicles && this.vehicles.length > 0) {

            // tslint:disable-next-line:forin
            for (const i in this.vehicles) {
              const position: number = parseInt(i, 10) + 1;
              const newObject: any = {'id': this.vehicles[i].id,
                                      'position' : position,
                                      'vehicle' : (this.vehicles[i].brand + ' ' + this.vehicles[i].model)};

              // tslint:disable-next-line:forin
              for (const j in this.mainServices) {
                for (const k in this.vehicles) {
                  if (this.mainServices[j][k] && this.mainServices[j][k].vehicle.id === this.vehicles[i].id) {
                    newObject[this.mainServices[j][k].rentACarMainServiceDTO.name + ''] = this.mainServices[j][k].price;
                    break;
                  }
                }
              }
              this.mainServicesDataSource.push(newObject);
            }

            this.columns = [
              { id: -1, columnDef: 'position', header: 'No.',     cell: (element: any) => `${element.position}` },
              { id: -1, columnDef: 'vehicle',  header: 'Vehicle', cell: (element: any) => `${element.vehicle}`  }
            ];

            const newColumns = [];
            for (const i in this.mainServices) {
              if (this.mainServices[i]) {
                let mainService: any;

                // tslint:disable-next-line:forin
                for (const j in this.mainServices[i]) {
                  mainService = this.mainServices[i][j];
                  break;
                }
                const newColumn = { id: mainService.rentACarMainServiceDTO.id,
                                    columnDef: mainService.rentACarMainServiceDTO.name + Math.random(),
                                    header: mainService.rentACarMainServiceDTO.name,
                                    cell: (element: any) => `${element[mainService.rentACarMainServiceDTO.name]}` };
                newColumns.push(newColumn);
                }
            }
            // newColumns = this.appComponent.sortArray(newColumns, 'header', true);
            this.columns = this.columns.concat(newColumns);
            this.dataSource = new MatTableDataSource<Element[]>();
            this.displayedColumns = this.columns.map(c => c.columnDef);
            this.dataSource.data = this.mainServicesDataSource;
            setTimeout(() => this.dataSource.paginator = this.paginator);
          }
        }
      );
    }
  }

  ngAfterViewInit() {
    this.rentACarService.getRentACarChartData(this.id).subscribe(
      (chartData: []) => {
        console.log(chartData);
        this.loadCharts(chartData);
       },
       () => {
        alert('JEBAIGA');
       }
     );
  }

  ngOnDestroy() {
    if (this.chart) {
      this.amCharts.destroyChart(this.chart);
    }
  }

  switchPage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
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
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openAddVehicleDialog() {
    const dialogRef = this.dialog.open(VehicleDialogComponent,
    {
      data: {
        'id' : this.rentACar.id,
        'vehicle' :  null,
        'mainServices' : null
      },
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          let newVehicle = data.vehicle;
          newVehicle.rentACar = this.rentACar;
          this.vehicleService.addVehicle(newVehicle).subscribe(
            (vehicle) => {
              newVehicle = vehicle;
              const vehicleToShow = newVehicle;
              vehicleToShow.rating = 0;
              this.vehicles.push(vehicleToShow);
              this.message = 'Added vehicle successfully!';

              // tslint:disable-next-line:forin
              for (const i in data.vehiclePrices) {
                data.vehiclePrices[i].vehicle = newVehicle;
              }
              this.vehicleService.addVehiclePrices(data.vehiclePrices).subscribe(
                () => {
                  this.fetchMainServicesPrices();
                },
                () => {
                }
              );

            },
            () => {
              this.message = 'Error! Vehicle could not be added!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openUpdateVehicleDialog(vehicle: any) {
    const mainServices = [];
    // tslint:disable-next-line:forin
    for (const i in this.mainServices) {
      for (const j in this.vehicles) {
        if (this.mainServices[i][j] && this.mainServices[i][j].vehicle.id === vehicle.id) {
          mainServices.push(this.mainServices[i][j]);
          break;
        }
      }
    }

    const dialogRef = this.dialog.open(VehicleDialogComponent,
      {
        data: {
          'id' : this.rentACar.id,
          'vehicle' :  vehicle,
          'mainServices' : mainServices
        },
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });
      dialogRef.afterClosed().subscribe(
        (data) => {
          if (data) {
            const updatedVehicle = data.vehicle;
            updatedVehicle.rentACar = this.rentACar;
            this.vehicleService.updateVehicle(updatedVehicle).subscribe(
              () => {
                for (const i in this.vehicles) {
                  if (this.vehicles[i].id === updatedVehicle.id) {
                    updatedVehicle.rating = this.vehicles[i].rating;
                    this.vehicles.splice(i, 1, updatedVehicle);
                    break;
                  }
                }
                this.message = 'Updated vehicle successfully!';

                // tslint:disable-next-line:forin
                for (const i in data.vehiclePrices) {
                  data.vehiclePrices[i].vehicle = updatedVehicle;
                }
                this.vehicleService.updateVehiclePrices(data.vehiclePrices).subscribe(
                  () => {
                    this.fetchMainServicesPrices();
                }
              );

              },
              () => {
                this.message = 'Error! Vehicle could not be updated!';
              },
              () => {
                this.appComponent.showSnackBar(this.message);
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

        this.fetchMainServicesPrices();

        this.message = 'Deleted vehicle successfully!';
      },
      () => {
        this.message = 'Error! Vehicle could not be deleted!';
      },
      () => {
        this.appComponent.showSnackBar(this.message);
      }
    );
  }

  openAddMainServiceDialog() {
    const dialogRef = this.dialog.open(RentACarMainServiceDialogComponent,
    {
      data: {
        'rentACarMainService' : null,
        'vehicles' : this.vehicles
      },
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          const newMainService = data.rentACarMainService;
          newMainService.rentACar = this.rentACar;
          this.rentACarService.addMainService(newMainService).subscribe(
            (mainService) => {
              // tslint:disable-next-line:forin
              for (const i in data.vehiclePrices) {
                data.vehiclePrices[i].rentACarMainServiceDTO = mainService;
              }
              this.vehicleService.addVehiclePrices(data.vehiclePrices).subscribe(
                () => {
                  this.fetchMainServicesPrices();
                }
              );
              this.message = 'Added service successfully!';
            },
            () => {
              this.message = 'Error! Service could not be added!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openUpdateMainServiceDialog(id: number) {
    if (isNaN(id)) {
      return;
    }
    let mainServiceToUpdate = null;
    for (const vehicleServices of this.mainServices) {
      if (vehicleServices) {
        for (const vehicleService of vehicleServices) {
          const tmp: any = vehicleService;
          if (vehicleService && tmp.rentACarMainServiceDTO.id === id) {
            mainServiceToUpdate = tmp.rentACarMainServiceDTO;
            break;
          }
        }
        if (mainServiceToUpdate) {
          break;
        }
      }
    }
    if (mainServiceToUpdate) {
      const dialogRef = this.dialog.open(RentACarMainServiceDialogComponent,
      {
        data: {
          'rentACarMainService' : mainServiceToUpdate,
          'vehicles' : null
        },
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });
      dialogRef.afterClosed().subscribe(
        (data) => {
          if (data) {
            const updatedMainService = data.rentACarMainService;
            updatedMainService.rentACar = this.rentACar;
            this.rentACarService.updateMainService(updatedMainService).subscribe(
              () => {
                this.fetchMainServicesPrices();
                this.message = 'Updated service successfully!';
              },
              () => {
                this.message = 'Error! Service could not be updated!';
              },
              () => {
                this.appComponent.showSnackBar(this.message);
              }
            );
          }
        }
      );
    } else {
      this.appComponent.showSnackBar('Error! Service could not be updated!');
    }
  }

  deleteMainService(id: number) {
    if (!isNaN(id)) {
      this.rentACarService.deleteMainService(id).subscribe(
        () => {
          this.message = 'Deleted service successfully!';
          this.fetchMainServicesPrices();
        },
        () => {
          this.message = 'Error! Service could not be deleted!';
        },
        () => {
          this.appComponent.showSnackBar(this.message);
        }
      );
    } else {
      this.appComponent.showSnackBar('Error! Service could not be deleted!');
    }
  }

  fetchMainServicesPrices() {
    this.rentACarService.getMainServicesPrices(this.rentACar.id).subscribe(
      (mainServices: [[]]) => {
        this.mainServices = mainServices;
        this.mainServicesDataSource = [];
        this.mainServicesLoaderSubject.next({key: 'loadedMainServices', value: true});
      }
    );
  }

  openAddOfficeDialog() {
    const dialogRef = this.dialog.open(OfficeDialogComponent,
    {
      data: null,
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          const newOffice = data;
          newOffice.rentACar = this.rentACar;
          this.rentACarService.addOffice(newOffice).subscribe(
            (office) => {
              this.offices.push(office);
              this.message = 'Added office successfully!';
            },
            () => {
              this.message = 'Error! Office could not be added!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openUpdateOfficeDialog(officeToUpdate: any) {
    const dialogRef = this.dialog.open(OfficeDialogComponent,
    {
      data: officeToUpdate,
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          const updatedOffice = data;
          updatedOffice.rentACar = this.rentACar;
          this.rentACarService.updateOffice(updatedOffice).subscribe(
            (office: any) => {
              this.offices.forEach((element, index) => {
                if (element.id === office.id) {
                  this.offices.splice(index, 1, updatedOffice);
                  return;
                }
              });
              this.message = 'Updated office successfully!';
            },
            () => {
              this.message = 'Error! Office could not be updated!';
            },
            () => {
              this.appComponent.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  deleteOffice(id: number) {
    if (!isNaN(id)) {
      this.rentACarService.deleteOffice(id).subscribe(
        () => {
          this.offices.forEach((element, index) => {
            if (element.id === id) {
              this.offices.splice(index, 1);
              return;
            }
          });
          this.message = 'Deleted office successfully!';
        },
        () => {
          this.message = 'Error! Office could not be deleted!';
        },
        () => {
          this.appComponent.showSnackBar(this.message);
        }
      );
    } else {
      this.appComponent.showSnackBar('Error! Office could not be deleted!');
    }
  }

  officeLocationChanged(office) {
    this.officeLocation = office.location.latitude + ', ' + office.location.longitude;
    this.updateOfficeLocationMap();
  }

  updateOfficeLocationMap() {
    this.officeMapUrl = this.sanitizer.bypassSecurityTrustResourceUrl('https://maps.google.com/maps?q=' +
                                                                      this.officeLocation +
                                                                      '&t=&z=11&ie=UTF8&iwloc=&output=embed');
  }

  loadCharts(chartData: []) {
    this.chart = this.amCharts.makeChart('chartdiv', {
        'type': 'serial',
        'theme': 'light',
        'marginRight': 40,
        'marginLeft': 40,
        'autoMarginOffset': 20,
        'mouseWheelZoomEnabled': true,
        'dataDateFormat': 'YYYY-MM-DD',
        'valueAxes': [{
            'id': 'v1',
            'axisAlpha': 0,
            'position': 'left',
            'ignoreAxisWidth': true
        }],
        'balloon': {
            'borderThickness': 1,
            'shadowAlpha': 0
        },
        'graphs': [{
            'id': 'g1',
            'balloon': {
              'drop': true,
              'adjustBorderColor': false,
              'color': '#ffffff'
            },
            'bullet': 'round',
            'bulletBorderAlpha': 1,
            'bulletColor': '#FFFFFF',
            'bulletSize': 5,
            'hideBulletsCount': 50,
            'lineThickness': 2,
            'title': 'red line',
            'useLineColorForBulletBorder': true,
            'valueField': 'value',
            'balloonText': '<span style=\'font-size:18px;\'>[[value]]</span>'
        }],
        'chartScrollbar': {
            'graph': 'g1',
            'oppositeAxis': false,
            'offset': 30,
            'scrollbarHeight': 80,
            'backgroundAlpha': 0,
            'selectedBackgroundAlpha': 0.1,
            'selectedBackgroundColor': '#888888',
            'graphFillAlpha': 0,
            'graphLineAlpha': 0.5,
            'selectedGraphFillAlpha': 0,
            'selectedGraphLineAlpha': 1,
            'autoGridCount': true,
            'color': '#AAAAAA'
        },
        'chartCursor': {
            'pan': true,
            'valueLineEnabled': true,
            'valueLineBalloonEnabled': true,
            'cursorAlpha': 1,
            'cursorColor': '#258cbb',
            'limitToGraph': 'g1',
            'valueLineAlpha': 0.2,
            'valueZoomable': true
        },
        'valueScrollbar': {
          'oppositeAxis': false,
          'offset': 50,
          'scrollbarHeight': 10
        },
        'categoryField': 'date',
        'categoryAxis': {
            'parseDates': true,
            'dashLength': 1,
            'minorGridEnabled': true
        },
        'export': {
            'enabled': true
        },
        'dataProvider': chartData
    }, 10);

    this.chart.addListener('rendered', this.zoomChart);
    this.zoomChart();
  }

  zoomChart() {
    if (this) {
      this.chart.zoomToIndexes(this.chart.dataProvider.length, this.chart.dataProvider.length);
    }
  }
}
