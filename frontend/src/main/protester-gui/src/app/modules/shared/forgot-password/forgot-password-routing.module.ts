import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RestoreRequestComponent} from "./restore-request/restore-request.component";
import {TokenSentComponent} from "./token-sent/token-sent.component";
import {CreateNewComponent} from "./create-new/create-new.component";
import {InvalidTokenComponent} from "./invalid-token/invalid-token.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'restore-request',
        pathMatch: 'full'
      },
      {
        path: 'restore-request',
        component: RestoreRequestComponent
      },
      {
        path: 'token-sent',
        component: TokenSentComponent
      },
      {
        path: 'create-new',
        component: CreateNewComponent
      },
      {
        path: 'invalid-token',
        component: InvalidTokenComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForgotPasswordRoutingModule { }
