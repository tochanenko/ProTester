import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {SearchComponent} from "./search/search.component";
import {ViewComponent} from "./view/view.component";
import {CreateComponent} from "./create/create.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Test Scenarios'
    },
    children: [
      {
        path: '',
        data: {
          breadcrumb: null
        },
        component: SearchComponent
      },
      {
        path: 'view',
        data: {
          breadcrumb: 'View'
        },
        component: ViewComponent
      },
      {
        path: 'new',
        data: {
          breadcrumb: 'Create'
        },
        component: CreateComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestScenarioRoutingModule { }
