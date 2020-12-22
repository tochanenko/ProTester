import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ListComponent} from "./list/list.component";
import {RunComponent} from './run/run.component';

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Test Cases'
    },
    children: [
      {
        path: '',
        data: {
          breadcrumb: null
        },
        component: ListComponent
      },
      {
        path: 'run',
        data: {
          breadcrumb: 'Run'
        },
        component: RunComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestCasesRoutingModule { }
