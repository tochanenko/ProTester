import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LibrariesMenuRoutingModule } from './libraries-menu-routing.module';
import { ViewComponent } from './view/view.component';
import {LibrariesModule} from "./libraries/libraries.module";
import { CompoundsModule } from "./compounds/compounds.module";
import {ActionsModule} from "./actions/actions.module";
import {MaterialModule} from "../../services/material.module";


@NgModule({
  declarations: [ViewComponent],
  imports: [
    CommonModule,
    LibrariesMenuRoutingModule,
    LibrariesModule,
    CompoundsModule,
    ActionsModule,
    MaterialModule
  ]
})
export class LibrariesMenuModule { }
