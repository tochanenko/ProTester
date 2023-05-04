import { TestBed } from '@angular/core/testing';

import { TestCaseResultService } from './test-case-result.service';

describe('TestCaseResultService', () => {
  let service: TestCaseResultService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestCaseResultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
