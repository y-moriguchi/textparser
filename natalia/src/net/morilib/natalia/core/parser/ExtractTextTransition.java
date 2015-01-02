/*
 * Copyright 2015 Yuichiro Moriguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.morilib.natalia.core.parser;

import java.util.EnumSet;

/**
 *
 */
public class ExtractTextTransition implements Transition {

	//
	static final Transition INSTANCE = new ExtractTextTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#transit(net.morilib.natalia.Quadro, net.morilib.natalia.ParserState)
	 */
	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case EXTRACT_TEXT_INIT:
			q.turnLeft().forward();
			return ParserState.EXTRACT_TEXT_MOVE1;
		case EXTRACT_TEXT_MOVE1:
			if(q.get().isJunction() && q.peekRight().isWall()) {
				q.setColumnSpanRegister(0);
				q.setRowSpanRegister(1);
				q.clearTextRegister();
				q.turnRight().forward();
				return ParserState.EXTRACT_TEXT_MOVE2;
			} else if(q.get().isWall() || q.get().isJunction()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		case EXTRACT_TEXT_MOVE2:
			if(q.get().isJunction() && q.peekRight().isWall()) {
				q.turnRight().forward();
				return ParserState.EXTRACT_TEXT_MOVE3;
			} else if(q.get().isJunction() || q.get().isWall()) {
				q.turnRight().forward().turnLeft();
				return ParserState.EXTRACT_TEXT_COLLECT;
			} else {
				throw new IllegalStateException();
			}
		case EXTRACT_TEXT_MOVE3:
			if(q.get().isJunction() && q.peekRight().isWall()) {
				q.turnLeft();
				return ParserState.EXTRACT_TEXT_END;
			} else if(q.get().isWall() || q.get().isJunction()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		case EXTRACT_TEXT_COLLECT:
			if(!q.get().isEqualsToLeft()) {
				q.appendTextRegister(q.get().getChar());
			}
			q.forward();

			if(q.get().isJunction() || q.get().isWall()) {
				q.turnLeft().turnLeft().forward();
				return ParserState.EXTRACT_TEXT_COLLECT_CR1;
			} else if(q.get().isBound()) {
				throw new IllegalStateException();
			}
			return state;
		case EXTRACT_TEXT_COLLECT_CR1:
			if(q.get().isWall() || q.get().isJunction()) {
				if(q.getScratch().isBorderCrossing()) {
					q.setRowSpanRegister(q.getRowSpanRegister() + 1);
				}
				q.appendTextRegister('\n');
				q.turnLeft().forward().turnLeft().forward();
				return ParserState.EXTRACT_TEXT_COLLECT_CR2;
			} else {
				q.forward();
			}
			return state;
		case EXTRACT_TEXT_COLLECT_CR2:
			if(q.get().isJunction() && q.peekLeft().isWall()) {
				return ParserState.EXTRACT_TEXT_END;
			} else if(q.get().isJunction() || q.get().isWall()) {
				return ParserState.EXTRACT_TEXT_COLLECT_CR3;
			} else {
				return ParserState.EXTRACT_TEXT_COLLECT;
			}
		case EXTRACT_TEXT_COLLECT_CR3:
			if(q.getScratch().isBorderCrossing()) {
				q.setColumnSpanRegister(
						q.getColumnSpanRegister() + 1);
			}

			if(q.get().isJunction() && q.peekLeft().isWall()) {
				q.getTableModelBuilder().appendCell(
						q.getRowRegister() - q.getRowSpanRegister() + 1,
						q.getColumnRegister(),
						q.getRowSpanRegister(),
						q.getColumnSpanRegister(),
						q.getTextRegister().trim());
				q.setColumnRegister(q.getColumnRegister() +
						q.getColumnSpanRegister() - 1);
				return ParserState.EXTRACT_TEXT_END;
			} else if(q.get().isJunction() || q.get().isWall()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		default:
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.EXTRACT_TEXT_INIT,
				ParserState.EXTRACT_TEXT_MOVE1,
				ParserState.EXTRACT_TEXT_MOVE2,
				ParserState.EXTRACT_TEXT_MOVE3,
				ParserState.EXTRACT_TEXT_COLLECT,
				ParserState.EXTRACT_TEXT_COLLECT_CR1,
				ParserState.EXTRACT_TEXT_COLLECT_CR2,
				ParserState.EXTRACT_TEXT_COLLECT_CR3,
				ParserState.EXTRACT_TEXT_END);
		return r;
	}

}
