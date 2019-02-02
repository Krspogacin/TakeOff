import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RentACarsComponent } from './rent-a-cars.component';

describe('RentACarsComponent', () => {
  let component: RentACarsComponent;
  let fixture: ComponentFixture<RentACarsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RentACarsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RentACarsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
