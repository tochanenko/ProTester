import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenSentComponent } from './token-sent.component';

describe('TokenSentComponent', () => {
  let component: TokenSentComponent;
  let fixture: ComponentFixture<TokenSentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TokenSentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenSentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
