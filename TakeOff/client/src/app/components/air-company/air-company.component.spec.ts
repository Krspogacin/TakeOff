import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AirCompanyComponent } from './air-company.component';

describe('AirCompanyComponent', () => {
  let component: AirCompanyComponent;
  let fixture: ComponentFixture<AirCompanyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AirCompanyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AirCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
