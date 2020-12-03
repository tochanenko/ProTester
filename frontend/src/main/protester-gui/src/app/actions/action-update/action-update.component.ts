import { Component, OnInit } from '@angular/core';
import {ACTIONS} from "../../mock-actions";
import {ActionService} from "../../services/action/action.service";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-action-update',
  templateUrl: './action-update.component.html',
  styleUrls: ['./action-update.component.css']
})
export class ActionUpdateComponent implements OnInit {

  actions;
  checkoutForm;

  constructor(
    private actionService: ActionService,
    private formBuilder: FormBuilder,
  ) {
    this.checkoutForm = this.formBuilder.group({
      description: '',
    });
  }

  ngOnInit() {
    this.actions = ACTIONS;
  }

  onSubmit(action) {
    // Process checkout data here
   // this.items = this.cartService.clearCart();
    this.checkoutForm.reset();

  }
}
