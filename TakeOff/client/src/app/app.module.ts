import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule, RoutingComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MaterialModule } from './material';
import { RegistrationDialogComponent } from './components/registration-dialog/registration-dialog.component';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginDialogComponent } from './components/login-dialog/login-dialog.component';
import { TokenInterceptor } from './http-interceptor';
import { VerifyUserComponent } from './components/verify-user/verify-user.component';
import { RentACarComponent } from './components/rent-a-car/rent-a-car.component';
import { RentACarDialogComponent } from './components/rent-a-car-dialog/rent-a-car-dialog.component';
import { VehicleDialogComponent } from './components/vehicle-dialog/vehicle-dialog.component';
import { LogoutComponent } from './components/logout/logout.component';
import { RentACarsComponent } from './components/rent-a-cars/rent-a-cars.component';
import { NameFilterPipe } from './pipes/name-filter.pipe';
import { RentACarMainServiceDialogComponent } from './components/rent-a-car-main-service-dialog/rent-a-car-main-service-dialog.component';
import { ReservationsComponent } from './components/reservations/reservations.component';
import { OfficeDialogComponent } from './components/office-dialog/office-dialog.component';
import { FriendsFilterPipe } from './pipes/friends-filter/friends-filter.pipe';
import { VehicleReservationDialogComponent } from './components/vehicle-reservation-dialog/vehicle-reservation-dialog.component';
import { RatingRoundPipe } from './pipes/rating-round/rating-round.pipe';
import { ChangePasswordDialogComponent } from './components/change-password-dialog/change-password-dialog.component';
import { AmChartsModule } from '@amcharts/amcharts3-angular';
import { LicationAndNamePipe } from './pipes/location-and-name/lication-and-name.pipe';

@NgModule({
    declarations: [
        AppComponent,
        RoutingComponents,
        NotFoundComponent,
        RegistrationDialogComponent,
        LoginDialogComponent,
        VerifyUserComponent,
        RentACarComponent,
        RentACarDialogComponent,
        VehicleDialogComponent,
        LogoutComponent,
        RentACarsComponent,
        NameFilterPipe,
        RentACarMainServiceDialogComponent,
        FriendsFilterPipe,
        OfficeDialogComponent,
        VehicleReservationDialogComponent,
        ReservationsComponent,
        OfficeDialogComponent,
        RatingRoundPipe,
        ChangePasswordDialogComponent,
        LicationAndNamePipe,
    ],
    entryComponents: [
        RoutingComponents,
        RegistrationDialogComponent,
        LoginDialogComponent,
        RentACarDialogComponent,
        VehicleDialogComponent,
        RentACarMainServiceDialogComponent,
        OfficeDialogComponent,
        VehicleReservationDialogComponent,
        ChangePasswordDialogComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        BrowserAnimationsModule,
        MaterialModule,
        ReactiveFormsModule,
        AmChartsModule
    ],
    providers: [{ provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }],
    bootstrap: [AppComponent]
})
export class AppModule { }
