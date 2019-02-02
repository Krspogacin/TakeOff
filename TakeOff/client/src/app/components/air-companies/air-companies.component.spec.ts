import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AirCompaniesComponent } from './air-companies.component';

describe('AirCompaniesComponent', () => {
  let component: AirCompaniesComponent;
  let fixture: ComponentFixture<AirCompaniesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AirCompaniesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AirCompaniesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
