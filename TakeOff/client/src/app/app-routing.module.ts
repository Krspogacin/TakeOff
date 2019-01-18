import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AirCompanyComponent } from './components/air-company/air-company.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

import { HotelComponent } from './components/hotel/hotel.component';
import { AddHotelModalComponent} from './components/add-hotel-modal/add-hotel-modal.component';
import { FlightComponent } from './components/flight/flight.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { SuccessfulRegistrationComponent } from './components/successful-registration/successful-registration.component';
import { LoginComponent } from './components/login/login.component';
import { VerifyUserComponent } from './components/verify-user/verify-user.component';
import { RentACarComponent } from './components/rent-a-car/rent-a-car.component';

const routes: Routes = [
  { path: 'companies/:id', component: AirCompanyComponent },
  { path: 'flights/:id', component: FlightComponent },
  { path: 'hotels', component : HotelComponent },
  { path: 'users/registration', component: RegistrationComponent },
  { path: 'users/successful_registration', component: SuccessfulRegistrationComponent },
  { path: 'users/register/verify', component: VerifyUserComponent },
  { path: 'users/login', component: LoginComponent },
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
                                  RentACarComponent];
