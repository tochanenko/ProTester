import { TestBed } from '@angular/core/testing';

import { LibraryManageService } from './library-manage.service';

describe('LibraryManageService', () => {
  let service: LibraryManageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LibraryManageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
