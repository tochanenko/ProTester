import { TestBed } from '@angular/core/testing';

import { PasswordService } from './password.service.ts.service';

describe('Password.Service.TsService', () => {
  let service: PasswordService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PasswordService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
