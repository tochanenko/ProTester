import {RouterModule, Routes} from "@angular/router";
import {TestCaseResultTableComponent} from "./test-case-result-table/test-case-result-table.component";
import {NgModule} from "@angular/core";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Test Case Result Table'
    },
    children: [
      {
        path: '',
        data: {
          breadcrumb: 'Table'
        },
        component: TestCaseResultTableComponent
      }
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestCaseRoutingModule { }
