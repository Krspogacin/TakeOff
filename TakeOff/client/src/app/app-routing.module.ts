import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AirCompanyComponent } from './components/air-company/air-company.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

import { HotelComponent } from './components/hotel/hotel.component';
import { AddHotelModalComponent } from './components/add-hotel-modal/add-hotel-modal.component';
import { FlightComponent } from './components/flight/flight.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { SuccessfulRegistrationComponent } from './components/successful-registration/successful-registration.component';
import { LoginComponent } from './components/login/login.component';
import { VerifyUserComponent } from './components/verify-user/verify-user.component';
import { RentACarComponent } from './components/rent-a-car/rent-a-car.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { AirCompanyDialogComponent } from './components/air-company-dialog/air-company-dialog.component';
import { FlightDialogComponent } from './components/flight-dialog/flight-dialog.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { HotelProfileComponent } from './components/hotel-profile/hotel-profile.component';
import { SysAdminDialogComponent } from './components/sys-admin-dialog/sys-admin-dialog.component';
import { HotelDialogComponent } from './components/hotel-dialog/hotel-dialog.component';
import { RoomDialogComponent } from './components/room-dialog/room-dialog.component';

const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'companies/:id', component: AirCompanyComponent },
  { path: 'flights/:id', component: FlightComponent },
  { path: 'hotels/:id', component: HotelProfileComponent },
  { path: 'users/registration', component: RegistrationComponent },
  { path: 'users/successful_registration', component: SuccessfulRegistrationComponent },
  { path: 'users/register/verify', component: VerifyUserComponent },
  { path: 'users/login', component: LoginComponent },
  { path: 'users/profile', component: UserProfileComponent },
  { path: 'rent-a-cars/:id', component: RentACarComponent },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

export const RoutingComponents = [AirCompanyComponent,
  NotFoundComponent,
  RegistrationComponent,
  FlightComponent,
  HotelComponent,
  SuccessfulRegistrationComponent,
  VerifyUserComponent,
  LoginComponent,
  RentACarComponent,
  HomepageComponent,
  AirCompanyDialogComponent,
  FlightDialogComponent,
  UserProfileComponent,
  HotelProfileComponent,
  SysAdminDialogComponent,
  HotelDialogComponent,
  RoomDialogComponent];
