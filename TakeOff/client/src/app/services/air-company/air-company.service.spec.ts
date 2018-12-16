import { TestBed } from '@angular/core/testing';

import { AirCompanyService } from './air-company.service';

describe('AirCompanyService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AirCompanyService = TestBed.get(AirCompanyService);
    expect(service).toBeTruthy();
  });
});
