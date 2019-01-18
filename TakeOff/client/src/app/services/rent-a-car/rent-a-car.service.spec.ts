import { TestBed } from '@angular/core/testing';

import { RentACarService } from './rent-a-car.service';

describe('RentACarService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RentACarService = TestBed.get(RentACarService);
    expect(service).toBeTruthy();
  });
});
