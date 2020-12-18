import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompoundNewComponent } from './compound-new.component';

describe('CompoundNewComponent', () => {
  let component: CompoundNewComponent;
  let fixture: ComponentFixture<CompoundNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompoundNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompoundNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
