import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RestoreRequestComponent} from "./restore-request/restore-request.component";
import {TokenSentComponent} from "./token-sent/token-sent.component";
import {CreateNewComponent} from "./create-new/create-new.component";
import {InvalidTokenComponent} from "./invalid-token/invalid-token.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Forgot password'
    },
    children: [
      {
        path: '',
        redirectTo: 'restore-request',
        pathMatch: 'full'
      },
      {
        path: 'restore-request',
        data: {
          breadcrumb: 'Restore request'
        },
        component: RestoreRequestComponent
      },
      {
        path: 'token-sent',
        data: {
          breadcrumb: 'Token sent'
        },
        component: TokenSentComponent
      },
      {
        path: 'create-new',
        data: {
          breadcrumb: 'Create new'
        },
        component: CreateNewComponent
      },
      {
        path: 'invalid-token',
        data: {
          breadcrumb: 'Invalid token'
        },
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
