import { TestBed } from '@angular/core/testing';

import { TestCaseRunAnalyzeService } from './test-case-run-analyze.service';

describe('TestCaseRunAnalyzeService', () => {
  let service: TestCaseRunAnalyzeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestCaseRunAnalyzeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
